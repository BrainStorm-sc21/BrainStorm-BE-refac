package com.brainstrom.meokjang.domain;

import com.brainstrom.meokjang.controller.UserForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "USER")
@Getter
@Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(length = 6, nullable = false)
    private String userName;
    @Column(length = 11)
    private String phoneNumber;
    @Column(length = 100)
    private String snsConnect;
    @Column(length = 40, nullable = false)
    private String location;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private int gender;
    @Column(nullable = false)
    private float reliability;
    @Column
    private Date stopUntil;
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    public User() {

    }

    public User(String userName, String phoneNumber, String snsConnect, String location,
                double latitude, double longitude, int gender) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.snsConnect = snsConnect;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gender = gender;
    }

    public User(UserForm userForm) {
        this.userName = userForm.getUserName();
        this.phoneNumber = userForm.getPhoneNumber();
        this.snsConnect = userForm.getSnsConnect();
        this.location = userForm.getLocation();
        this.latitude = userForm.getLatitude();
        this.longitude = userForm.getLongitude();
        this.gender = userForm.getGender();
        this.reliability = 50;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
