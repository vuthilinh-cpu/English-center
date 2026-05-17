package com.englishcenter.controller;

import com.englishcenter.dto.*;
import com.englishcenter.entity.User;
import com.englishcenter.repository.UserRepository;
import com.englishcenter.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Bước 1: Tìm user theo email
        Optional<User> userOpt = userRepo.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "Email hoặc mật khẩu sai"));
        }

        User user = userOpt.get();

        // Bước 2: Kiểm tra tài khoản có bị khóa không
        if (!user.getIsActive()) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "Tài khoản đã bị khóa"));
        }

       // BƯỚC 3: So sánh mật khẩu
System.out.println("=== DEBUG ===");
System.out.println("Email: " + request.getEmail());
System.out.println("Password nhập: " + request.getPassword());
System.out.println("Hash trong DB: " + user.getPasswordHash());
boolean match = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
System.out.println("Kết quả match: " + match);
System.out.println("=============");

if (!match) {
    return ResponseEntity.status(401)
        .body(Map.of("message", "Email hoặc mật khẩu sai"));
}

        // Bước 4: Tạo JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        // Bước 5: Trả về token + thông tin user
        return ResponseEntity.ok(new LoginResponse(
            token,
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole()
        ));
    }

    @GetMapping("/test")
public ResponseEntity<?> test() {
    System.out.println("=== TEST ENDPOINT ĐƯỢC GỌI ===");
    return ResponseEntity.ok(Map.of("message", "API hoạt động!"));
}
@GetMapping("/generate-hash")
public ResponseEntity<?> generateHash() {
    String hash = passwordEncoder.encode("123456");
    return ResponseEntity.ok(Map.of("hash", hash));
}
}