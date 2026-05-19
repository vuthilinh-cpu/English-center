package com.englishcenter.controller;

import com.englishcenter.entity.Attendances;
import com.englishcenter.repository.AttendancesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendancesController {

    @Autowired
    private AttendancesRepository attendancesRepository;

    // Lấy danh sách điểm danh của 1 buổi học cụ thể
    // Giúp giáo viên xem lại hôm đó ai vắng, ai có mặt
    @GetMapping("/session/{sessionId}")
    public List<Attendances> getAttendancesBySession(@PathVariable Long sessionId) {
        return attendancesRepository.findBySessionId(sessionId);
    }

    // Lưu bản ghi điểm danh cho một học sinh trong một buổi học
    @PostMapping
    public Attendances markAttendance(@RequestBody Attendances attendance) {
        return attendancesRepository.save(attendance);
    }
}