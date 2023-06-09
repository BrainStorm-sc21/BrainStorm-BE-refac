package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    private final Long sender;
    private final String message;
    private final LocalDateTime time;

    public ChatMessageResponse(ChatMessage message) {
        this.sender = message.getSender();
        this.message = message.getMessage();
        this.time = message.getTime();
    }
}
