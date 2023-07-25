package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.user.domain.User;
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
    private Long cruId;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "chat_user_id", referencedColumnName = "user_id", nullable = false)
    private User chatUser;
}
