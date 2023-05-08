package com.brainstrom.meokjang.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    private String phoneNumber;
    private String snsType;
    private String snsKey;
}
