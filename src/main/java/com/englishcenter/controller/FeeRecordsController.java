package com.englishcenter.controller;

import com.englishcenter.entity.FeeRecords;
import com.englishcenter.repository.FeeRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-records")
public class FeeRecordsController {

    @Autowired
    private FeeRecordsRepository feeRecordsRepository;

    // Lấy danh sách tất cả các hóa đơn học phí của 1 học sinh cụ thể
    // Dùng cho màn hình Tài chính của phụ huynh/học sinh
    @GetMapping("/student/{studentId}")
    public List<FeeRecords> getFeesByStudent(@PathVariable Long studentId) {
        return feeRecordsRepository.findByStudentId(studentId);
    }

    // Tạo một hóa đơn học phí mới (khi học sinh bắt đầu đăng ký khóa học)
    @PostMapping
    public FeeRecords createFeeRecord(@RequestBody FeeRecords feeRecord) {
        return feeRecordsRepository.save(feeRecord);
    }
}