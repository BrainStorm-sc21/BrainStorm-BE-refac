package com.brainstrom.meokjang.review.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private final Long reviewId;
    private final Long reviewFrom;
    private final Long reviewTo;
    private final Long dealId;
    private final Float rating;
    private final String reviewContent;
    private final LocalDateTime createdAt;

    public ReviewResponse(Long reviewId, Long reviewFrom, Long reviewTo, Long dealId, Float rating, String reviewContent, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
        this.dealId = dealId;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
    }
}
