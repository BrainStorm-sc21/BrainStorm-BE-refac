package com.brainstrom.meokjang.admin.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "ADMIN")
@Getter
@NoArgsConstructor
public class Admin {

    @Id @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(name = "admin_name", length = 10, nullable = false, unique = true)
    private String adminName;

    @Column(name = "admin_pw", length = 30, nullable = false)
    private String adminPw;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
