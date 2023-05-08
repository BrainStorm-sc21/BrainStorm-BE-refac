package com.brainstrom.meokjang.food.dto.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class OcrRequest {
    private String type;
    private MultipartFile image;

    public OcrRequest(String type, MultipartFile image) {
        this.type = type;
        this.image = image;
    }
}



