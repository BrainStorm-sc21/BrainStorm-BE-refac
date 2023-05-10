package com.brainstrom.meokjang.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "USER")
@Getter
@NoArgsConstructor
public class User {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", length = 6, nullable = false)
    private String userName;

    @Column(name = "phone_number", length = 11, unique = true)
    private String phoneNumber;

    @Column(name = "sns_type", length = 5)
    private String snsType;

    @Column(name = "sns_key", length = 100, unique = true)
    private String snsKey;

    @Column(name = "location", length = 40, nullable = false)
    private String location;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "reliability", nullable = false)
    private Float reliability;

    @Column(name = "stop_until")
    private LocalDate stopUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public User(Long userId, String userName, String phoneNumber, String snsType, String snsKey, String location,
                Double latitude, Double longitude, Integer gender, Float reliability, LocalDate stopUntil) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.snsType = snsType;
        this.snsKey = snsKey;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gender = gender;
        this.reliability = reliability;
        this.stopUntil = stopUntil;
    }
}
