package com.englishcenter.controller;

import com.englishcenter.dto.*;
import com.englishcenter.entity.User;
import com.englishcenter.repository.UserRepository;
import com.englishcenter.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired 
    private UserRepository userRepo;
    
    @Autowired 
    private JwtUtil jwtUtil;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    // ==========================================
    // API ĐĂNG NHẬP
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
        // SỬA LẠI: Dùng user.isActive() thay vì getIsActive(), và không check null
        if (!user.isActive()) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Tài khoản đã bị khóa"));
        }

        // Bước 3: So sánh mật khẩu (Dùng thuật toán băm Bcrypt)
        boolean match = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        
        if (!match) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Email hoặc mật khẩu sai"));
        }

        // Bước 4: Tạo JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        // Bước 5: Trả về token + thông tin user
        // Chú ý: Dùng getFullname() để khớp với Entity
        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getFullname(), 
                user.getEmail(),
                user.getRole()
        ));
    }

  
    // API ĐĂNG KÝ (REGISTER)
   
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", "Email này đã được sử dụng!"));
        }

        // Tạo mới tài khoản
        User newUser = new User();
        // SỬA LẠI: Dùng setFullname để khớp với Entity
        newUser.setFullname(request.getFullName()); 
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        // Cài đặt các giá trị mặc định
        newUser.setRole("STUDENT"); 
        newUser.setActive(true); // SỬA LẠI: Dùng setActive(true) thay vì setIsActive(true)
        newUser.setCreatedAt(LocalDateTime.now()); // Lưu lại thời gian tạo tài khoản

        userRepo.save(newUser);

        return ResponseEntity.ok(Map.of("message", "Đăng ký tài khoản thành công!"));
    }

    // ==========================================
    // API TEST TẠO MẬT KHẨU MÃ HÓA
    
    @GetMapping("/generate-hash")
    public ResponseEntity<?> generateHash(@RequestParam(defaultValue = "123456") String rawPassword) {
        String hash = passwordEncoder.encode(rawPassword);
        return ResponseEntity.ok(Map.of(
            "rawPassword", rawPassword,
            "hash", hash
        ));
    }
}