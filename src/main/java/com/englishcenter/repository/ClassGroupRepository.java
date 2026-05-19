package com.englishcenter.repository;

import com.englishcenter.entity.ClassGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroups, Long> {
    //Tìm tất cả các nhóm lớp
    List<ClassGroups> findAll();
}
