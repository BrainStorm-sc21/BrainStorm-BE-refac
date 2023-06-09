package com.brainstrom.meokjang.deal.controller;

import com.brainstrom.meokjang.common.dto.response.ApiResponse;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class DealController {

    private final DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping("/deal/{userId}/around")
    public ResponseEntity<ApiResponse> getDealList(@PathVariable Long userId) {

        try {
            ApiResponse res = new ApiResponse(200, "거래 목록 조회 성공", dealService.aroundDealList(userId));
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 목록 조회 실패", e.getMessage()));
        }
    }

    @PostMapping("/deal")
    public ResponseEntity<ApiResponse> createDeal(@ModelAttribute DealRequest dealRequest, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 생성 실패", result.getAllErrors()));
        }
        try {
            dealService.save(dealRequest);
            ApiResponse res = new ApiResponse(200, "거래 생성 성공", null);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 생성 실패", e.getMessage()));
        }
    }

    @GetMapping("/deal/{dealId}/{userId}")
    public ResponseEntity<ApiResponse> getDealInfo(@PathVariable Long dealId, @PathVariable Long userId) {

        try {
            DealInfoResponse dealInfoResponse = dealService.getDealInfo(dealId, userId);
            ApiResponse res = new ApiResponse(200, "거래 정보 조회 성공", dealInfoResponse);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 정보 조회 실패", e.getMessage()));
        }
    }

    @PutMapping("/deal/{dealId}")
    public ResponseEntity<ApiResponse> updateDealInfo(@PathVariable Long dealId, @RequestBody DealRequest dealRequest, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 정보 수정 실패", result.getAllErrors()));
        }
        try {
            dealService.updateDealInfo(dealId, dealRequest);
            ApiResponse res = new ApiResponse(200, "거래 정보 수정 성공", null);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 정보 수정 실패", e.getMessage()));
        }
    }

    @DeleteMapping("/deal/{dealId}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long dealId) {

        try {
            dealService.deleteDeal(dealId);
            ApiResponse res = new ApiResponse(200, "거래 삭제 성공", null);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 삭제 실패", e.getMessage()));
        }
    }

    @GetMapping("/deal/{userId}/my")
    public ResponseEntity<ApiResponse> getMyDealList(@PathVariable Long userId) {

        try {
            ApiResponse res = new ApiResponse(200, "내 거래 목록 조회 성공", dealService.myDealList(userId));
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "내 거래 목록 조회 실패", e.getMessage()));
        }
    }

    @PutMapping("/deal/{dealId}/complete")
    public ResponseEntity<ApiResponse> completeDeal(@PathVariable Long dealId) {

        try {
            dealService.completeDeal(dealId);
            ApiResponse res = new ApiResponse(200, "거래 완료 성공", null);
            return ResponseEntity.ok(res);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 완료 실패", e.getMessage()));
        }
    }

    @GetMapping("/deal/{dealId}/complete")
    public ResponseEntity<ApiResponse> chatDealUserList(@PathVariable Long dealId) {
        try {
            ApiResponse result = new ApiResponse(200, "거래 채팅 유저 목록 조회 성공", dealService.getChatDealUser(dealId));
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "거래 채팅 유저 목록 조회 실패", e.getMessage()));
        }
    }
}
