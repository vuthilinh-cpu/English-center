package com.englishcenter.repository;

import com.englishcenter.entity.ClassLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassLevelRepository extends JpaRepository<ClassLevel, Integer> {

    // Spring tự viết SQL — chỉ cần đặt tên method đúng quy tắc
    List<ClassLevel> findByStatus(String status);
    // => SELECT * FROM ClassLevels WHERE status = ?

    List<ClassLevel> findByYearOrderByGradeAsc(Integer year);
    // => SELECT * FROM ClassLevels WHERE year = ? ORDER BY grade ASC
}
