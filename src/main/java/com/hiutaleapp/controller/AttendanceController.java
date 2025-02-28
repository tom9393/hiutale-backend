package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AttendanceDTO;
import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.service.AttendanceService;
import com.hiutaleapp.service.EventService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private EventService eventService;

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

            return attendanceService.createAttendance(attendance);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create attendance due to foreign key error; either event doesn't exist or you have already attended it");
        }
    }

//    @PutMapping("/update/{id}")
//    public AttendanceDTO updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
//        return attendanceService.updateAttendance(id, attendance);
//    }

    @DeleteMapping("/delete/{id}")
    public void deleteAttendance(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Optional<AttendanceDTO> review = attendanceService.getAttendanceByUserIdAndEventId(Long.parseLong(auth.getName()), id);
            if (review.isPresent()) {
                attendanceService.deleteAttendance(review.get().getId());
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