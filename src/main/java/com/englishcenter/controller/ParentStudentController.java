package com.englishcenter.controller;

import com.englishcenter.entity.ParentStudent;
import com.englishcenter.repository.ParentStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent-student")
public class ParentStudentController {

    @Autowired
    private ParentStudentRepository parentStudentRepository;

    // Lấy danh sách các học sinh (con cái) của một phụ huynh cụ thể
    // Dùng để phụ huynh đăng nhập vào App và xem thông tin của các con
    @GetMapping("/parent/{parentId}")
    public List<ParentStudent> getStudentsByParent(@PathVariable Long parentId) {
        return parentStudentRepository.findByParentId(parentId);
    }

    // Liên kết tài khoản phụ huynh với tài khoản học sinh
    @PostMapping
    public ParentStudent linkParentAndStudent(@RequestBody ParentStudent parentStudent) {
        return parentStudentRepository.save(parentStudent);
    }
}