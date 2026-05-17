package com.englishcenter.controller;

import com.englishcenter.entity.ClassLevel;
import com.englishcenter.repository.ClassLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*")  // cho phép frontend JS gọi vào
public class ClassController {

    @Autowired
    private ClassLevelRepository classRepo;

    // =============================================
    // GET /api/classes
    // Lấy toàn bộ danh sách lớp
    // =============================================
    @GetMapping
    public ResponseEntity<List<ClassLevel>> getAllClasses() {
        List<ClassLevel> classes = classRepo.findAll();
        return ResponseEntity.ok(classes);
    }

    // =============================================
    // GET /api/classes/active
    // Chỉ lấy lớp đang hoạt động
    // =============================================
    @GetMapping("/active")
    public ResponseEntity<List<ClassLevel>> getActiveClasses() {
        List<ClassLevel> classes = classRepo.findByStatus("ACTIVE");
        return ResponseEntity.ok(classes);
    }

    // =============================================
    // POST /api/classes
    // Tạo lớp mới
    // =============================================
    @PostMapping
    public ResponseEntity<ClassLevel> createClass(@RequestBody ClassLevel classLevel) {
        classLevel.setStatus("ACTIVE"); // mặc định khi tạo mới
        ClassLevel saved = classRepo.save(classLevel);
        return ResponseEntity.status(201).body(saved);
    }

    // =============================================
    // PATCH /api/classes/{id}/close
    // Đóng lớp — không xóa dữ liệu
    // =============================================
    @PatchMapping("/{id}/close")
    public ResponseEntity<?> closeClass(@PathVariable Integer id) {
        return classRepo.findById(id)
            .map(cl -> {
                cl.setStatus("CLOSED");
                classRepo.save(cl);
                return ResponseEntity.ok(Map.of("message", "Đã đóng lớp thành công"));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}