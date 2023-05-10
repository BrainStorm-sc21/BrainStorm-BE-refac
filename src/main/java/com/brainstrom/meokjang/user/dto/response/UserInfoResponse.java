package com.brainstrom.meokjang.user.dto.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoResponse {

    private Long userId;
    private String userName;
    private String location;
    private Double latitude;
    private Double longitude;
    private Float reliability;
    private LocalDate stopUntil;

    public UserInfoResponse(Long userId, String userName, String location, Double latitude, Double longitude,
                            Float reliability, LocalDate stopUntil) {
        this.userId = userId;
        this.userName = userName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reliability = reliability;
        this.stopUntil = stopUntil;
    }
}
