package com.brainstrom.meokjang.deal.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class DealRequest {

    private Long userId;
    private Integer dealType;
    private String dealName;
    private String dealContent;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;
}
