package com.englishcenter.repository;

import com.englishcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    // Spring tự viết SQL:
    // SELECT * FROM Users WHERE email = ?
    List<User> findByRole(String role);
// SELECT * FROM Users WHERE role = ?

List<User> findByIsActive(Boolean isActive);
// SELECT * FROM Users WHERE is_active = ?
}