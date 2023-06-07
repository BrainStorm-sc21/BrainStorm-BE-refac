package com.brainstrom.meokjang.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatRoomRequest {
    private Long sender;
    private Long receiver;

    public ChatRoomRequest(Long sender, Long receiver){
        this.sender = sender;
        this.receiver = receiver;
    }
}


