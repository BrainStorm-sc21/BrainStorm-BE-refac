package com.brainstrom.meokjang.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewRequest {

    private Long reviewFrom;
    private Long reviewTo;
    private Float rating;
    private String reviewContent;

    public ReviewRequest(Long reviewFrom, Long reviewTo, Float rating, String reviewContent) {
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }
}
