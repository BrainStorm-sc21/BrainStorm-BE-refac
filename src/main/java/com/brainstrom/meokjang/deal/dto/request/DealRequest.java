package com.brainstrom.meokjang.deal.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DealRequest {

    private Long userId;
    private Integer dealType;
    private String dealName;
    private String dealContent;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
}
