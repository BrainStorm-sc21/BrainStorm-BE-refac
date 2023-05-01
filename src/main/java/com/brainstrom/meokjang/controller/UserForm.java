package com.brainstrom.meokjang.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
    private String userName;
    private String phoneNumber;
    private String snsConnect;
    private String location;
    private double latitude;
    private double longitude;
    private int gender;
}
