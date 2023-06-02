package com.brainstrom.meokjang.deal.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DealInfoResponse {

    private Long dealId;
    private Long userId;
    private Integer dealType;
    private String dealName;
    private String dealContent;
    private Double latitude;
    private Double longitude;
    private Double distance;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private Boolean isClosed;
    private LocalDateTime createdAt;

    public DealInfoResponse(Long dealId, Long userId, Integer dealType,String dealName, String dealContent, Double latitude, Double longitude,
                            Double distance, String image1, String image2, String image3, String image4, Boolean isClosed, LocalDateTime createdAt) {
        this.dealId = dealId;
        this.userId = userId;
        this.dealType = dealType;
        this.dealName = dealName;
        this.dealContent = dealContent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.isClosed = isClosed;
        this.createdAt = createdAt;
    }
}
