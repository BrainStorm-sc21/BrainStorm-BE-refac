package com.brainstrom.meokjang.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewRequest {

    private final Long reviewFrom;
    private final Long reviewTo;
    private final Float rating;
    private final String reviewContent;

    public ReviewRequest(Long reviewFrom, Long reviewTo, Float rating, String reviewContent) {
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }
}
