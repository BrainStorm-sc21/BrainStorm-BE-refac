package com.brainstrom.meokjang.deal.service;

import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DealService {

    private static final double EARTH_RADIUS = 6371;
    private final DealRepository dealRepository;

    @Autowired
    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public void save(DealRequest dealRequest) {
        try {
            dealRepository.save(dealRequest.toEntity());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<DealInfoResponse> aroundDealList(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            List<Deal> deals = dealRepository.findAroundDealList(deal.getLatitude(), deal.getLongitude());
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                dealLists.add(d.toResponse());
            }
            return dealLists;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public DealInfoResponse getDealInfo(Long dealId) {
        try {
            return dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."))
                    .toResponse();
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
}
