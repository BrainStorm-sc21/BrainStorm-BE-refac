package com.brainstrom.meokjang.deal.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class DealRequest {

    private final Long userId;
    private final Integer dealType;
    private final String dealName;
    private final String dealContent;
    private final MultipartFile image1;
    private final MultipartFile image2;
    private final MultipartFile image3;
    private final MultipartFile image4;

    @Builder
    public DealRequest(Long userId, Integer dealType, String dealName, String dealContent,
                       MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4) {
        this.userId = userId;
        this.dealType = dealType;
        this.dealName = dealName;
        this.dealContent = dealContent;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }
}
