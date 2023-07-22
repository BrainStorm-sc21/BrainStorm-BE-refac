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

    @Column(name = "user_name", length = 6, nullable = false, unique = true)
    private String userName;

    @Column(name = "phone_number", length = 11, nullable = false,unique = true)
    private String phoneNumber;

    @Column(name = "location", length = 40, nullable = false)
    private String location;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "reliability", nullable = false)
    private Float reliability;

    @Column(name = "stop_until")
    private LocalDate stopUntil;

    @Column(name = "firebase_token", length = 255, nullable = false, unique = true)
    private String firebaseToken;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public User(Long userId, String userName, String phoneNumber, String location, Double latitude, Double longitude,
                Float reliability, LocalDate stopUntil, String firebaseToken) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reliability = reliability;
        this.stopUntil = stopUntil;
        this.firebaseToken = firebaseToken;
    }

    public void suspend(int suspensionDays) {
        this.stopUntil = LocalDate.now().plusDays(suspensionDays);
    }
}
