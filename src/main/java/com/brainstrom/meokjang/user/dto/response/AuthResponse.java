package com.brainstrom.meokjang.user.dto.response;

import lombok.Getter;

@Getter // 수정하기
public class AuthResponse {
    private Long userId;

    public AuthResponse(Long userId) {
        this.userId = userId;
    }
}
