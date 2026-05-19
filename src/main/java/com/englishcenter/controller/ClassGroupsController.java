package com.englishcenter.controller;

import com.englishcenter.entity.ClassGroups;
import com.englishcenter.repository.ClassGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-groups")
public class ClassGroupsController {

    @Autowired
    private ClassGroupRepository classGroupsRepository;

    // Lấy danh sách tất cả các khối lớp (Ví dụ: Khối IELTS, Khối TOEIC, Khối Giao tiếp)
    @GetMapping
    public List<ClassGroups> getAllGroups() {
        return classGroupsRepository.findAll();
    }

    // Tạo mới một khối lớp
    @PostMapping
    public ClassGroups createGroup(@RequestBody ClassGroups group) {
        return classGroupsRepository.save(group);
    }
}