package com.englishcenter.controller;

import com.englishcenter.entity.Sessions;
import com.englishcenter.repository.SessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    @Autowired
    private SessionsRepository sessionsRepository;

    // Lấy toàn bộ danh sách các buổi học của một lớp cụ thể
    // Dùng cho màn hình Lịch học của Lớp
    @GetMapping("/class/{classId}")
    public List<Sessions> getSessionsByClass(@PathVariable Long classId) {
        return sessionsRepository.findByClassId(classId);
    }

    // Tạo mới một buổi học (hoặc tạo tự động từ thời khóa biểu)
    @PostMapping
    public Sessions createSession(@RequestBody Sessions session) {
        return sessionsRepository.save(session);
    }
}