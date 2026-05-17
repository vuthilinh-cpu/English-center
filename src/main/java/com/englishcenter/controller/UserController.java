package com.englishcenter.controller;

import com.englishcenter.entity.User;
import com.englishcenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    // =============================================
    // GET /api/users — lấy tất cả users
    // =============================================
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    // =============================================
    // GET /api/users/role/{role} — lấy theo role
    // VD: /api/users/role/teacher → danh sách GV
    // =============================================
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userRepo.findByRole(role));
    }

    // =============================================
    // POST /api/users — tạo user mới
    // =============================================
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400)
                .body(Map.of("message", "Email đã tồn tại"));
        }
        // Mã hóa mật khẩu trước khi lưu
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setIsActive(true);
        return ResponseEntity.status(201).body(userRepo.save(user));
    }

    // =============================================
    // PUT /api/users/{id} — cập nhật thông tin
    // =============================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody User updated) {
        return userRepo.findById(id)
            .map(user -> {
                user.setFullName(updated.getFullName());
                user.setPhone(updated.getPhone());
                user.setRole(updated.getRole());
                // Chỉ đổi mật khẩu nếu có gửi lên
                if (updated.getPasswordHash() != null
                        && !updated.getPasswordHash().isEmpty()) {
                    user.setPasswordHash(
                        passwordEncoder.encode(updated.getPasswordHash()));
                }
                return ResponseEntity.ok(userRepo.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // =============================================
    // PATCH /api/users/{id}/deactivate — khóa tài khoản
    // Không xóa — chỉ đặt is_active = false
    // =============================================
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        return userRepo.findById(id)
            .map(user -> {
                user.setIsActive(false);
                userRepo.save(user);
                return ResponseEntity.ok(Map.of("message", "Đã khóa tài khoản"));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
