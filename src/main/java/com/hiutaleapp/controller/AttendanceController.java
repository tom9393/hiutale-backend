package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AttendanceDTO;
import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.AttendanceService;
import com.hiutaleapp.service.EventService;
import com.hiutaleapp.service.NotificationService;
import com.hiutaleapp.util.AttendanceForm;
import com.hiutaleapp.util.DataViolationException;
import com.hiutaleapp.util.DatabaseConnectionException;
import com.hiutaleapp.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceService.getAllAttendances();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public List<AttendanceDTO> getAttendanceByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return attendanceService.getAttendanceByUser(Long.parseLong(auth.getName()));
    }

    @PostMapping("/create")
    public AttendanceDTO createAttendance(@RequestBody AttendanceForm attendanceForm) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Attendance attendance = new Attendance();
            User user = new User();
            Event event = new Event();

            user.setUserId(Long.parseLong(auth.getName()));
            event.setEventId(attendanceForm.getId());

            // Too bad we need two queries however I don't want to create a special tracker for amount spots left; shouldn't matter for project of this scope
            // Ideally we should also run this after checking of duplicate registration, maybe will fix this later
            Optional<EventDTO> eventDTO = eventService.getEventById(attendanceForm.getId());
            long count = attendanceService.countByEventId(attendanceForm.getId());
            if (eventDTO.isPresent()) {
                if (count >= eventDTO.get().getCapacity()) {
                    throw new DataViolationException("Could not register attendance; event already full");
                }
            } else {
                throw new DataViolationException("Could not find event with this id");
            }

            attendance.setUser(user);
            attendance.setEvent(event);

            Notification notification = new Notification();
            User user2 = new User();
            user2.setUserId(eventDTO.get().getOrganizerId());
            notification.setUser(user2);
            notification.setReadStatus(false);
            notification.setDisplayAfter(new Date());
            notification.setMessage("Someone has registered to your event!");


            // Subtract 24 hours from event start time
            Date eventStart = eventDTO.get().getStart();
            Instant displayAfterInstant = eventStart.toInstant().minus(24, ChronoUnit.HOURS);
            Notification notification2 = new Notification();
            User user3 = new User();
            user3.setUserId(Long.parseLong(auth.getName()));
            notification2.setUser(user3);
            notification2.setReadStatus(false);
            notification2.setDisplayAfter(Date.from(displayAfterInstant));
            notification2.setMessage("You have an upcoming event in less than 24h!");

            notificationService.createNotification(notification);
            notificationService.createNotification(notification2);
            return attendanceService.createAttendance(attendance);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create attendance due to foreign key error; either event doesn't exist or you have already attended it");
        }
    }

    @PutMapping("/update/{id}")
    public AttendanceDTO updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(id, attendance);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAttendance(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<AttendanceDTO> attendance = attendanceService.getAttendanceByUserIdAndEventId(Long.parseLong(auth.getName()), id);
            if (attendance.isPresent()) {
                attendanceService.deleteAttendance(attendance.get().getId());
            } else {
                throw new NotFoundException("Event with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this attendance because other data is dependent upon it");
        }
    }
}