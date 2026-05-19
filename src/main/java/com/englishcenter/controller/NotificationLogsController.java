package com.englishcenter.controller;

import com.englishcenter.entity.NotificationLogs;
import com.englishcenter.repository.NotificationLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-logs")
public class NotificationLogsController {

    @Autowired
    private NotificationLogsRepository notificationLogsRepository;

    // Lấy danh sách thông báo đã gửi tới một người dùng cụ thể (Hộp thư đến)
    @GetMapping("/user/{recipientId}")
    public List<NotificationLogs> getLogsByUser(@PathVariable Long recipientId) {
        return notificationLogsRepository.findByRecipientId(recipientId);
    }

    // Hệ thống ghi lại lịch sử mỗi khi gửi thành công 1 thông báo/Email/SMS
    @PostMapping
    public NotificationLogs createLog(@RequestBody NotificationLogs log) {
        return notificationLogsRepository.save(log);
    }
}