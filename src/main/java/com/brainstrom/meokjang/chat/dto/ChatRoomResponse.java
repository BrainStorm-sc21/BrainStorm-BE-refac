package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.deal.domain.Deal;
import com.brainstrom.meokjang.deal.dto.response.DealInfoResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {
    private final Long id;
    private final String roomId;
    private final DealInfoResponse dealInfo;
    private final Long sender;
    private final Long receiver;
    private final String lastMessage;
    private final LocalDateTime lastTime;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        Deal deal = chatRoom.getDeal();
        DealInfoResponse dealResponse = DealInfoResponse.builder()
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
                .isClosed(deal.getIsClosed())
                .createdAt(deal.getCreatedAt())
                .build();
        this.dealInfo = dealResponse;
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
        this.lastMessage = chatRoom.getLastMessage();
        this.lastTime = chatRoom.getLastTime();

    }
}
