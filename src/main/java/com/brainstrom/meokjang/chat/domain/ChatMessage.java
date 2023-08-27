package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import com.brainstrom.meokjang.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_MESSAGE")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id @Column(name = "chat_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "user_id", nullable = false)
    private User sender;

    @Column(name = "message", length = 300, nullable = false)
    private String message;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

//    public static ChatMessage toEntity(ChatMessageDto chatMessageDto, ChatRoom chatRoom, User sender){
//        ChatMessage chatMessageEntity = new ChatMessage();
//        chatMessageEntity.setChatRoom(chatRoom);
//        chatMessageEntity.setSender(sender);
//        chatMessageEntity.setMessage(chatMessageDto.getMessage());
//        chatMessageEntity.setTime(chatMessageDto.getTime());
//        return chatMessageEntity;
//    }
}