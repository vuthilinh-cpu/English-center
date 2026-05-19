package com.englishcenter.controller;

import com.englishcenter.entity.CmsBanners;
import com.englishcenter.repository.CmsBannersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms-banners")
public class CmsBannersController {

    @Autowired
    private CmsBannersRepository cmsBannersRepository;

    // Lấy danh sách các Banner đang ở trạng thái hiển thị (Active)
    // Dùng cho màn hình Trang chủ của Frontend (Web/Mobile App)
    @GetMapping("/active")
    public List<CmsBanners> getActiveBanners() {
        return cmsBannersRepository.findByIsActiveTrue();
    }

    // Admin tải lên và tạo cấu hình cho một Banner mới
    @PostMapping
    public CmsBanners createBanner(@RequestBody CmsBanners banner) {
        return cmsBannersRepository.save(banner);
    }
}