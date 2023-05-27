package com.brainstrom.meokjang.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatDto {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
