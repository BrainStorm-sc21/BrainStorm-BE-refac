package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    private String phoneNumber;
    private String snsType;
    private String snsKey;
}
