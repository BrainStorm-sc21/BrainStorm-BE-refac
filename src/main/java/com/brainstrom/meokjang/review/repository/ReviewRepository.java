package com.brainstrom.meokjang.review.repository;

import com.brainstrom.meokjang.review.domain.Review;
import com.brainstrom.meokjang.review.dto.response.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByReviewTo(Long reviewTo);

    List<Review> findAllByReviewFrom(Long reviewFrom);
}
