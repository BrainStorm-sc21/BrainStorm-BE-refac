package com.brainstrom.meokjang.review.service;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import com.brainstrom.meokjang.notice.dto.FCMNotificationRequestDto;
import com.brainstrom.meokjang.notice.service.FCMNotificationService;
import com.brainstrom.meokjang.review.domain.Review;
import com.brainstrom.meokjang.review.dto.request.ReviewRequest;
import com.brainstrom.meokjang.review.dto.response.ReviewResponse;
import com.brainstrom.meokjang.review.repository.ReviewRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ReviewService {

    private final FCMNotificationService noticeService;
    private final ReviewRepository reviewRepository;
    private final DealRepository dealRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(FCMNotificationService noticeService,
                         ReviewRepository reviewRepository,
                         DealRepository dealRepository,
                         UserRepository userRepository) {
        this.noticeService = noticeService;
        this.reviewRepository = reviewRepository;
        this.dealRepository = dealRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse save(Long dealId, ReviewRequest reviewRequest) {
        if (reviewRequest.getRating() < 0 || reviewRequest.getRating() > 5) {
            throw new IllegalStateException("평점은 0점 이상 5점 이하로 입력해주세요.");
        } else if (reviewRequest.getReviewContent().length() > 1000) {
            throw new IllegalStateException("리뷰 내용은 1000자 이하로 입력해주세요.");
        } else if (reviewRequest.getReviewContent().length() == 0) {
            throw new IllegalStateException("리뷰 내용을 입력해주세요.");
        } else if (Objects.equals(reviewRequest.getReviewFrom(), reviewRequest.getReviewTo())) {
            throw new IllegalStateException("자신에게 리뷰를 작성할 수 없습니다.");
        } else {
            User reviewFrom = userRepository.findById(reviewRequest.getReviewFrom())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            User reviewTo = userRepository.findById(reviewRequest.getReviewTo())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            Review review = Review.builder()
                    .reviewFrom(reviewFrom.getUserId())
                    .reviewTo(reviewTo.getUserId())
                    .dealId(deal.getDealId())
                    .rating(reviewRequest.getRating())
                    .reviewContent(reviewRequest.getReviewContent())
                    .build();
            reviewRepository.save(review);
            updateReliability(reviewTo.getUserId(), reviewRequest.getRating());
            sendNotice(dealId, reviewRequest, noticeService);
            return buildReviewResponse(reviewFrom, reviewTo, review);
        }
    }

    public void sendNotice(Long dealId, ReviewRequest reviewRequest, FCMNotificationService noticeService) {
        Map<String, String> data = new HashMap<>();
        data.put("type", String.valueOf(reviewRepository.countByDealId(dealId)));
        data.put("sender", String.valueOf(reviewRequest.getReviewFrom()));

//        Deal deal = dealRepository.findById(dealId).orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
        User user = userRepository.findById(reviewRequest.getReviewFrom()).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        String reviewFrom = user.getUserName();

        FCMNotificationRequestDto noticeRequestDto = FCMNotificationRequestDto.builder()
                .targetUserId(reviewRequest.getReviewTo())
                .title("후기가 도착했어요!")
                .body("\\uD83D\\uDCE9 " + reviewFrom + "님으로부터 도착한 후기를 확인해보세요")
                .data(data)
                .build();
        noticeService.sendNotificationByToken(noticeRequestDto);
    }

    public Map<String, List<ReviewResponse>> getReviewList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        List<Review> reviewFrom = reviewRepository.findAllByReviewFrom(user.getUserId());
        List<Review> reviewTo = reviewRepository.findAllByReviewTo(user.getUserId());
        List<ReviewResponse> reviewFromRes = new ArrayList<>();
        List<ReviewResponse> reviewToRes = new ArrayList<>();
        for (Review review : reviewFrom) {
            User reviewToUser = userRepository.findById(review.getReviewTo())
                    .orElseThrow(()-> new IllegalStateException("존재하지 않는 유저입니다."));
            reviewFromRes.add(buildReviewResponse(user, reviewToUser, review));
        }
        for (Review review : reviewTo) {
            User reviewFromUser = userRepository.findById(review.getReviewFrom())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            reviewToRes.add(buildReviewResponse(reviewFromUser, user, review));
        }
        return Map.of(
                "reviewFrom", reviewFromRes,
                "reviewTo", reviewToRes
        );
    }

    private ReviewResponse buildReviewResponse(User reviewFromUser, User reviewTouser, Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reviewFrom(review.getReviewFrom())
                .reviewFromName(reviewFromUser.getUserName())
                .reviewFromReliability(reviewFromUser.getReliability())
                .reviewTo(review.getReviewTo())
                .reviewToName(reviewTouser.getUserName())
                .reviewToReliability(reviewTouser.getReliability())
                .dealId(review.getDealId())
                .dealName(dealRepository.findById(review.getDealId())
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다.")).getDealName())
                .rating(review.getRating())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public void updateReliability(Long reviewTo, Float rating) {
        User user = userRepository.findById(reviewTo).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        int result = userRepository.updateReliabilityById(reviewTo, user.getReliability() + rating);
        if (result != 1) {
            throw new IllegalStateException("신뢰도 업데이트에 실패하였습니다.");
        }
    }
}
