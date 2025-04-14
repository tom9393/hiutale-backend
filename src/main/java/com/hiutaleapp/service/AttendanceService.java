package com.hiutaleapp.service;

import com.hiutaleapp.dto.AttendanceDTO;
import com.hiutaleapp.dto.EventDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.entity.Event;
import com.hiutaleapp.entity.Notification;
import com.hiutaleapp.entity.User;
import com.hiutaleapp.repository.AttendanceRepository;
import com.hiutaleapp.util.forms.AttendanceForm;
import com.hiutaleapp.util.errors.DataViolationException;
import com.hiutaleapp.util.errors.DatabaseConnectionException;
import com.hiutaleapp.util.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AttendanceDTO> getAttendanceById(Long id) {
        return attendanceRepository.findById(id).map(this::mapToDTO);
    }

    public List<AttendanceDTO> getAttendanceByUser(Long id) {
        return attendanceRepository.findByUser_UserId(id).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AttendanceDTO> getAttendanceByUserIdAndEventId(Long userId, Long eventId) {
        return attendanceRepository.findByUser_UserIdAndEvent_EventId(userId, eventId).map(this::mapToDTO);
    }

    public AttendanceDTO createAttendance(long userId, AttendanceForm attendanceForm) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Attendance attendance = new Attendance();
            User user = new User();
            Event event = new Event();

            user.setUserId(userId);
            event.setEventId(attendanceForm.getId());

            // Too bad we need two queries however I don't want to create a special tracker for amount spots left; shouldn't matter for project of this scope
            // Ideally we should also run this after checking of duplicate registration, maybe will fix this later
            Optional<EventDTO> eventDTO = eventService.getEventById(attendanceForm.getId());
            long count = countByEventId(attendanceForm.getId());
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
            return mapToDTO(attendance);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not create attendance due to foreign key error; either event doesn't exist or you have already attended it");
        }
    }

    public AttendanceDTO updateAttendance(Long id, Attendance attendance) {
        attendance.setAttendeeId(id);
        return mapToDTO(attendanceRepository.save(attendance));
    }

    public Boolean deleteAttendance(Long userId, Long id) {
        attendanceRepository.deleteById(id);
        try {
            Optional<AttendanceDTO> attendance = getAttendanceByUserIdAndEventId(userId, id);
            if (attendance.isPresent()) {
                attendanceRepository.deleteById(id);
                return true;
            } else {
                throw new NotFoundException("Event with this ID does not exist");
            }
        } catch (DataAccessResourceFailureException e) {
            throw new DatabaseConnectionException("Could not connect to the database");
        } catch (DataIntegrityViolationException e) {
            throw new DataViolationException("Could not delete this attendance because other data is dependent upon it");
        }
    }

    public AttendanceDTO mapToDTO(Attendance attendance) {
        return new AttendanceDTO(attendance);
    }

    public Attendance mapToEntity(AttendanceDTO attendanceDTO) {
        Attendance attendance = new Attendance();
        attendance.setCreatedAt(attendanceDTO.getRegistrationTime());
        return attendance;
    }

    public long countByEventId(Long eventId) {
        return attendanceRepository.countByEvent_EventId(eventId);
    }
}