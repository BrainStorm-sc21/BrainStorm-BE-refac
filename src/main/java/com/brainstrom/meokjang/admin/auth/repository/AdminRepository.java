package com.brainstrom.meokjang.admin.auth.repository;

import com.brainstrom.meokjang.admin.auth.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminName(String adminId);
}
