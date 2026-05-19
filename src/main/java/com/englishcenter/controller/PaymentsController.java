package com.englishcenter.controller;

import com.englishcenter.entity.Payments;
import com.englishcenter.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsRepository paymentsRepository;

    // Xem chi tiết các lần thanh toán của một Hóa đơn học phí
    // (Vì học sinh có thể đóng học phí chia làm nhiều đợt)
    @GetMapping("/fee-record/{feeRecordId}")
    public List<Payments> getPaymentsByFeeRecord(@PathVariable Long feeRecordId) {
        return paymentsRepository.findByFeeRecordId(feeRecordId);
    }

    // Ghi nhận một giao dịch thanh toán mới (Chuyển khoản, tiền mặt...)
    @PostMapping
    public Payments processPayment(@RequestBody Payments payment) {
        return paymentsRepository.save(payment);
    }
}