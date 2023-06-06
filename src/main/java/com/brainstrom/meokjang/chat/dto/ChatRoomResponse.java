package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private final Long id;
    private final String roomId;
    private final Long sender;
    private final Long receiver;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();

    }
}
