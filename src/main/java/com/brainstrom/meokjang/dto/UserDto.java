package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class UserDto {

    private long userId;
    private String userName;
    private String phoneNumber;
    private String snsConnect;
    private String location;
    private double latitude;
    private double longitude;
    private int gender;
    private float reliability;
    private Date stopUntil;
    private Timestamp createdAt;
}
