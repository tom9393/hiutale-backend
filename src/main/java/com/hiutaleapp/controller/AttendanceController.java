package com.hiutaleapp.controller;

import com.hiutaleapp.dto.AttendanceDTO;
import com.hiutaleapp.entity.Attendance;
import com.hiutaleapp.service.AttendanceService;
import com.hiutaleapp.util.UserContext;
import com.hiutaleapp.util.forms.AttendanceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

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
        return attendanceService.getAttendanceByUser(UserContext.getUserId());
    }

    @PostMapping("/create")
    public AttendanceDTO createAttendance(@RequestBody AttendanceForm attendanceForm) {
        return attendanceService.createAttendance(UserContext.getUserId(), attendanceForm);
    }

    @PutMapping("/update/{id}")
    public AttendanceDTO updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(id, attendance);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteAttendance(@PathVariable Long id) {
        return attendanceService.deleteAttendance(UserContext.getUserId(), id);
    }
}