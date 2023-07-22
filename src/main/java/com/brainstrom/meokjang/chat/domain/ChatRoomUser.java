package com.brainstrom.meokjang.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "CHAT_ROOM_USER")
@Getter
@NoArgsConstructor
public class ChatRoomUser {

    @Id @Column(name = "cru_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cruId;

    @Column(name = "room_id", nullable = false, length = 25)
    private String roomId;

    @Column(name = "chat_user_id", nullable = false)
    private Integer chatUserId;
}
