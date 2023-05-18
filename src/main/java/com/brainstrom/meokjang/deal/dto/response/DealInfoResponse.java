package com.brainstrom.meokjang.deal.dto.response;

import lombok.Getter;

@Getter
public class DealInfoResponse {

    private Long dealId;
    private Long userId;
    private String dealName;
    private String dealContent;
    private String location;
    private Double latitude;
    private Double longitude;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    public DealInfoResponse(Long dealId, Long userId, String dealName, String dealContent, String location, Double latitude, Double longitude, String image1, String image2, String image3, String image4) {
        this.dealId = dealId;
        this.userId = userId;
        this.dealName = dealName;
        this.dealContent = dealContent;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }
}
