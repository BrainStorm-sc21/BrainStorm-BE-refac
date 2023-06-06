package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "CHAT_MESSAGE")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private Long sender;

    @Column
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime time;

    public static ChatMessage toEntity(ChatMessageDto chatMessageDTO, ChatRoom chatRoom){
        ChatMessage chatMessageEntity = new ChatMessage();
        chatMessageEntity.setChatRoom(chatRoom);
        chatMessageEntity.setSender(chatMessageDTO.getSender());
        chatMessageEntity.setMessage(chatMessageDTO.getMessage());

        return chatMessageEntity;

    }
}