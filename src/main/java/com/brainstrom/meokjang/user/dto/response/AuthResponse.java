package com.brainstrom.meokjang.user.dto.response;

import lombok.Getter;

@Getter
public class AuthResponse {
    private Long userId;

    public AuthResponse(Long userId) {
        this.userId = userId;
    }
}
