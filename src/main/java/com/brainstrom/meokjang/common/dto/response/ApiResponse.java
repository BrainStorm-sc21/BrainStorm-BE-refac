package com.brainstrom.meokjang.common.dto.response;

public class ApiResponse {
    private final Integer status;
    private final String message;
    private final Object data;

    public ApiResponse(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
