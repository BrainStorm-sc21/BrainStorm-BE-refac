package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "CHAT_MESSAGE")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private String sender;

    @Column
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime sendDate;

    public static ChatMessage toEntity(ChatMessageDto chatMessageDto, ChatRoom chatRoom){
        ChatMessage chatMessageEntity = new ChatMessage();

        chatMessageEntity.setChatRoom(chatRoom);

        chatMessageEntity.setSender(chatMessageDto.getSender());
        chatMessageEntity.setMessage(chatMessageDto.getMessage());

        return chatMessageEntity;

    }
}