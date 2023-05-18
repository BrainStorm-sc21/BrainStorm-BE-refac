package com.brainstrom.meokjang.deal.dto.request;

import com.brainstrom.meokjang.deal.domain.Deal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DealRequest {

    private Long userId;
    private Integer dealType;
    private String dealName;
    private String dealContent;
    private String location;
    private Double latitude;
    private Double longitude;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    public Deal toEntity() {
        return Deal.builder()
                .userId(userId)
                .dealType(dealType)
                .dealName(dealName)
                .dealContent(dealContent)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .image4(image4)
                .isClosed(false)
                .isDeleted(false)
                .build();
    }
}
