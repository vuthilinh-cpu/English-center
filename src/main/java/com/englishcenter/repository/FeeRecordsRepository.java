package com.englishcenter.repository;

import com.englishcenter.entity.FeeRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeeRecordsRepository extends JpaRepository<FeeRecords, Long> {
    // Tìm hóa đơn học phí của một học sinh
    List<FeeRecords> findByStudentId(Long studentId);
    
    // Tìm các hóa đơn theo trạng thái (PAID - Đã nộp, UNPAID - Chưa nộp)
    List<FeeRecords> findByStatus(String status);
    
    // Tìm hóa đơn chưa thanh toán của một học sinh cụ thể
    List<FeeRecords> findByStudentIdAndStatus(Long studentId, String status);
}