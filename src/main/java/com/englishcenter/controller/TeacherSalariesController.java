package com.englishcenter.controller;

import com.englishcenter.entity.TeacherSalaries;
import com.englishcenter.repository.TeacherSalariesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher-salaries")
public class TeacherSalariesController {

    @Autowired
    private TeacherSalariesRepository teacherSalariesRepository;

    // Lấy lịch sử nhận lương của một giáo viên cụ thể
    @GetMapping("/teacher/{teacherId}")
    public Optional<TeacherSalaries> getSalariesByTeacher(@PathVariable Long teacherId) {
        return teacherSalariesRepository.findById(teacherId);
    }

    // Kế toán tạo bản ghi lương mới cho giáo viên vào cuối tháng
    @PostMapping
    public TeacherSalaries addSalaryRecord(@RequestBody TeacherSalaries salary) {
        return teacherSalariesRepository.save(salary);
    }
}