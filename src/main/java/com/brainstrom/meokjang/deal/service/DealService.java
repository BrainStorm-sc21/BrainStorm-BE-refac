package com.brainstrom.meokjang.deal.service;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.chat.repository.ChatRoomRepository;
import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.request.DealRequest;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import com.brainstrom.meokjang.deal.repository.DealRepository;
import com.brainstrom.meokjang.user.domain.User;
import com.brainstrom.meokjang.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class DealService {

    private final BucketService bucketService;
    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRepository;

    @Autowired
    public DealService(BucketService bucketService, DealRepository dealRepository,
                       UserRepository userRepository, ChatRoomRepository chatRepository) {
        this.bucketService = bucketService;
        this.dealRepository = dealRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    public void save(DealRequest dealRequest) {
        try {
            User user = userRepository.findById(dealRequest.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            String[] imageList = bucketService.uploadImage(dealRequest);
            Deal deal = Deal.builder()
                    .userId(dealRequest.getUserId())
                    .dealType(dealRequest.getDealType())
                    .dealName(dealRequest.getDealName())
                    .dealContent(dealRequest.getDealContent())
                    .location(user.getLocation())
                    .latitude(user.getLatitude())
                    .longitude(user.getLongitude())
                    .image1(imageList[0] != null ? imageList[0] : null)
                    .image2(imageList[1] != null ? imageList[1] : null)
                    .image3(imageList[2] != null ? imageList[2] : null)
                    .image4(imageList[3] != null ? imageList[3] : null)
                    .isDeleted(false)
                    .build();
            dealRepository.save(deal);
        } catch (IllegalStateException | IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<DealInfoResponse> aroundDealList(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            List<Deal> deals = dealRepository.findAroundDealList(user.getLatitude(), user.getLongitude());
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                if (d.getIsDeleted()) {
                    continue;
                }
                User dealUser = userRepository.findById(d.getUserId())
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
                Double distance = getDistance(user.getLatitude(), user.getLongitude(), d.getLatitude(), d.getLongitude());
                DealInfoResponse res = buildDealInfoResponse(d, dealUser, distance);
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
            User user = userRepository.findById(deal.getUserId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            Double distance = getDistance(user.getLatitude(), user.getLongitude(), deal.getLatitude(), deal.getLongitude());
            return buildDealInfoResponse(deal, user, distance);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void updateDealInfo(Long dealId, DealRequest dealRequest) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            String[] imageList = bucketService.uploadImage(dealRequest);
            deal.update(dealRequest.getDealType(), dealRequest.getDealName(), dealRequest.getDealContent(), imageList);
        } catch (IllegalStateException | IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void deleteDeal(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            String[] imageList = deal.getImageList();
            for (String image : imageList) {
                if (image != null)
                    if (!bucketService.deleteImage(image))
                        throw new IllegalStateException("이미지 삭제에 실패했습니다.");
            }
            deal.delete();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<DealInfoResponse> myDealList(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
            List<Deal> deals = dealRepository.findByUserId(userId);
            List<DealInfoResponse> dealLists = new ArrayList<>();
            for (Deal d : deals) {
                if (d.getIsDeleted()) {
                    continue;
                }
                DealInfoResponse res = buildDealInfoResponse(d, user, 0.0);
                dealLists.add(res);
            }
            return dealLists;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void completeDeal(Long dealId) {
        try {
            Deal deal = dealRepository.findById(dealId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 거래입니다."));
            deal.complete();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public Map<Long, String> getChatDealUser(Long dealId) {
        List<ChatRoom> chatRooms = chatRepository.findByDeal_DealId(dealId);
        if (chatRooms.size() == 0) {
            return null;
        }
        Map<Long, String> chatUserMap = new HashMap<>();
        for (ChatRoom chatRoom : chatRooms) {
            Long userId = chatRoom.getSender();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않거나 탈퇴한 유저이기 때문에 후기를 보낼 수 없습니다."));
            chatUserMap.put(userId, user.getUserName());
        }
        return chatUserMap;
    }

    private Double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

    public DealInfoResponse buildDealInfoResponse(Deal deal, User user, Double distance) {
        return DealInfoResponse.builder()
                .dealId(deal.getDealId())
                .userId(deal.getUserId())
                .userName(user.getUserName())
                .reliability(user.getReliability())
                .dealType(deal.getDealType())
                .dealName(deal.getDealName())
                .dealContent(deal.getDealContent())
                .latitude(deal.getLatitude())
                .longitude(deal.getLongitude())
                .distance(distance)
                .image1(deal.getImage1())
                .image2(deal.getImage2())
                .image3(deal.getImage3())
                .image4(deal.getImage4())
                .isClosed(deal.getIsClosed())
                .createdAt(deal.getCreatedAt())
                .build();
    }
}
