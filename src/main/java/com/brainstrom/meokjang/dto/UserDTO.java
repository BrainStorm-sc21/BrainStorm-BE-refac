package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String userName;
    private String phoneNumber;
    private String snsType;
    private String snsKey;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer gender;
    private Float reliability;
    private Date stopUntil;
    private Timestamp createdAt;
}
