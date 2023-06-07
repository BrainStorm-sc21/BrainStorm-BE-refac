package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {
    private final Long id;
    private final String roomId;
    private final Long sender;
    private final Long receiver;
    private final String lastMessage;
    private final LocalDateTime lastTime;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
        this.lastMessage = chatRoom.getLastMessage();
        this.lastTime = chatRoom.getLastTime();

    }
}
