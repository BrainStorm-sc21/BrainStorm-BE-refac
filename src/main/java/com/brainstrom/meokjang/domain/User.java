package com.brainstrom.meokjang.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", length = 6, nullable = false)
    private String userName;

    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(name = "sns_type", length = 5)
    private String snsType;

    @Column(name = "sns_key", length = 100)
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
    private Date stopUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

}
