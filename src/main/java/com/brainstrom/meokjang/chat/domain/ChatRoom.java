package com.brainstrom.meokjang.chat.domain;

import com.brainstrom.meokjang.chat.dto.ChatRoomDto;
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
@Table(schema = "CHAT_ROOM")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cr_id")
    private Long crId;

    @Column(name = "room_id", nullable = false, unique = true, length = 25)
    private String roomId;

    @ManyToOne
    @JoinColumn(name = "deal_id", referencedColumnName = "deal_id", nullable = false)
    private Deal deal;

    @Column(name = "last_massage", nullable = false, length = 300)
    private String lastMessage;

    @Column(name = "last_time", nullable = false)
    private LocalDateTime lastTime;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER)
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
