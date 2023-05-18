package com.brainstrom.meokjang.deal.service;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DealService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;

    @Autowired
    public DealService(DealRepository dealRepository, UserRepository userRepository) {
        this.dealRepository = dealRepository;
        this.userRepository = userRepository;
    }

    public void save(DealRequest dealRequest) {
        try {
            User user = userRepository.findByUserId(dealRequest.getUserId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            Deal deal = Deal.builder()
                    .userId(dealRequest.getUserId())
                    .dealType(dealRequest.getDealType())
                    .dealName(dealRequest.getDealName())
                    .dealContent(dealRequest.getDealContent())
                    .location(user.getLocation())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .image1(dealRequest.getImage1())
                    .image2(dealRequest.getImage2())
                    .image3(dealRequest.getImage3())
                    .image4(dealRequest.getImage4())
                    .build();
            dealRepository.save(deal);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<DealInfoResponse> aroundDealList(Long userId) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            List<Deal> deals = dealRepository.findAroundDealList(user.getLatitude(), user.getLongitude());
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                Double distance = getDistance(user.getLatitude(), user.getLongitude(), d.getLatitude(), d.getLongitude());
                DealInfoResponse res = DealInfoResponse.builder()
                        .dealId(d.getDealId())
                        .userId(d.getUserId())
                        .dealType(d.getDealType())
                        .dealName(d.getDealName())
                        .dealContent(d.getDealContent())
                        .latitude(d.getLatitude())
                        .longitude(d.getLongitude())
                        .distance(distance)
                        .image1(d.getImage1())
                        .image2(d.getImage2())
                        .image3(d.getImage3())
                        .image4(d.getImage4())
                        .createdAt(d.getCreatedAt())
                        .build();
                dealLists.add(res);
            }
            return dealLists;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public DealInfoResponse getDealInfo(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            return DealInfoResponse.builder()
                    .dealId(deal.getDealId())
                    .userId(deal.getUserId())
                    .dealType(deal.getDealType())
                    .dealName(deal.getDealName())
                    .dealContent(deal.getDealContent())
                    .latitude(deal.getLatitude())
                    .longitude(deal.getLongitude())
                    .distance(null)
                    .image1(deal.getImage1())
                    .image2(deal.getImage2())
                    .image3(deal.getImage3())
                    .image4(deal.getImage4())
                    .createdAt(deal.getCreatedAt())
                    .build();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void updateDealInfo(Long dealId, DealRequest dealRequest) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            deal.update(dealRequest);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void deleteDeal(Long dealId) {
        try {
            dealRepository.deleteById(dealId);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371;
        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c * 1000; // 결과 단위 미터(m)
        return distance;
    }
}
