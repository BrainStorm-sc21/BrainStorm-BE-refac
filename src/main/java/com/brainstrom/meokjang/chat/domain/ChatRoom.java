package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.deal.domain.Deal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHAT_ROOM")
public class ChatRoom {

    @Id @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crId;

    @Column(name = "room_id", length = 25, nullable = false, unique = true)
    private String roomId;

    @ManyToOne
    @JoinColumn(name = "deal_id", referencedColumnName = "deal_id", nullable = false)
    private Deal deal;

    @Column(name = "last_massage", length = 300)
    private String lastMessage;

    @Column(name = "last_time")
    private LocalDateTime lastTime;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

//    public static ChatRoom toEntity(ChatRoomDto chatRoomDto, Deal deal) {
//        ChatRoom chatRoomEntity = new ChatRoom();
//        chatRoomEntity.setRoomId(chatRoomDto.getRoomId());
//        chatRoomEntity.setDeal(deal);
//        chatRoomEntity.setLastMessage(chatRoomDto.getLastMessage());
//        chatRoomEntity.setLastTime(chatRoomDto.getLastTime());
//        return chatRoomEntity;
//    }
}
