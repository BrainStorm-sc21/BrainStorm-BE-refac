package com.brainstrom.meokjang.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateRequest {

    private String userName;

    public UserUpdateRequest(String userName) {
        this.userName = userName;
    }
}
