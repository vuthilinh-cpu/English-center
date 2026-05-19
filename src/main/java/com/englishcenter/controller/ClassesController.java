package com.englishcenter.controller;

import com.englishcenter.entity.Classes;
import com.englishcenter.repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {

    @Autowired
    private ClassesRepository classesRepository;

    // Lấy tất cả các lớp học hiện có trong hệ thống
    @GetMapping
    public List<Classes> getAllClasses() {
        return classesRepository.findAll();
    }

    // Tìm các lớp học dựa trên trạng thái (Ví dụ: ACTIVE - Đang mở, COMPLETED - Đã kết thúc)
    @GetMapping("/status/{status}")
    public List<Classes> getClassesByStatus(@PathVariable String status) {
        return classesRepository.findByStatus(status);
    }

    // Thêm một lớp học mới vào hệ thống
    @PostMapping
    public Classes createClass(@RequestBody Classes classes) {
        return classesRepository.save(classes); // save() tự động tạo câu lệnh INSERT xuống DB
    }
}