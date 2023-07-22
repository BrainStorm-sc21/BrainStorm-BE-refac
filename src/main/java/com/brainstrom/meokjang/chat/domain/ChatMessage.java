package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(schema = "CHAT_MESSAGE")
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Integer sender;

    @Column(nullable = false, length = 300)
    private String message;

    @Column
    private LocalDateTime time;

    public static ChatMessage toEntity(ChatMessageDto chatMessageDTO, ChatRoom chatRoom){
        ChatMessage chatMessageEntity = new ChatMessage();
//        chatMessageEntity.setChatRoom(chatRoom);
//        chatMessageEntity.setSender(chatMessageDTO.getSender());
//        chatMessageEntity.setMessage(chatMessageDTO.getMessage());
//        chatMessageEntity.setTime(chatMessageDTO.getTime());
        return chatMessageEntity;
    }
}