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
    @Builder.Default
    private MultipartFile image1 = null;
    @Builder.Default
    private MultipartFile image2 = null;
    @Builder.Default
    private MultipartFile image3 = null;
    @Builder.Default
    private MultipartFile image4 = null;

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
