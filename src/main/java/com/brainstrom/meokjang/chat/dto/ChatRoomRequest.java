package com.brainstrom.meokjang.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatRoomRequest {
    private Long dealId;
    private Long sender;
    private Long receiver;

    public ChatRoomRequest(Long dealId, Long sender, Long receiver){
        this.dealId = dealId;
        this.sender = sender;
        this.receiver = receiver;
    }
}


