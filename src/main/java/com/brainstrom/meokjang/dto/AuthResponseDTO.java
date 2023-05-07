package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {

    private String status;
    private String message;
    private Long userId;
}
