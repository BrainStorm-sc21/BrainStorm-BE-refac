package com.brainstrom.meokjang.food.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class OcrRequest {
    private String type;
    private MultipartFile image;

    public OcrRequest(String type, MultipartFile image) {
        this.type = type;
        this.image = image;
    }
}



