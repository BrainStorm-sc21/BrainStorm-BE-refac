package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String sender;
    private String receiver;
    public ChatRoomDto(String sender, String receiver){
        this.roomId = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
    }

    public ChatRoomDto(ChatRoom chatRoom){
        this.roomId = chatRoom.getRoomId();
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
    }

    public ChatRoom toEntity(){
        ChatRoom chatRoomEntity = new ChatRoom();
        chatRoomEntity.setRoomId(this.roomId);
        chatRoomEntity.setSender(this.sender);
        chatRoomEntity.setReceiver(this.receiver);


        return chatRoomEntity;
    }

}