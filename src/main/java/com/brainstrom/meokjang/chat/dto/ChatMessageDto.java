package com.brainstrom.meokjang.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType type;
    private String roomId;
    private Long sender;
    private String message;
}