package com.brainstrom.meokjang.chat.dto;

import com.brainstrom.meokjang.chat.domain.ChatMessage;
import com.brainstrom.meokjang.chat.service.ChatService;
import com.brainstrom.meokjang.notice.dto.FCMNotificationRequestDto;
import com.brainstrom.meokjang.notice.service.FCMNotificationService;
import com.brainstrom.meokjang.user.service.UserService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Getter
public class ChatRoomDto {
    private String roomId;
    private Long dealId;
    private Long sender;
    private Long receiver;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoomDto(String roomId, Long dealId, Long sender, Long receiver){
        this.roomId = roomId;
        this.dealId = dealId;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void handleAction(Set<WebSocketSession> beforeSessions, WebSocketSession session, ChatMessageDto message, ChatService service, FCMNotificationService noticeService, UserService userService) {
        if (message.getType().equals(ChatMessageDto.MessageType.ENTER)) {
            sessions.removeAll(beforeSessions);
            sessions.add(session);
            System.out.println("session" + sessions);
        } else if (message.getType().equals(ChatMessageDto.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            try {
                sendMessage(message, service);
                sendNotice(message, noticeService, userService);
                service.saveMessage(ChatMessage.toEntity(message, service.findByRoomId(message.getRoomId())));
                service.updateRoom(service.findByRoomId(message.getRoomId()), message.getMessage(), message.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(ChatMessageDto message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }

    public void sendNotice(ChatMessageDto message, FCMNotificationService noticeService, UserService userService) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "0");
        data.put("roomId", String.valueOf(message.getRoomId()));

        FCMNotificationRequestDto noticeRequestDto = FCMNotificationRequestDto.builder()
                .targetUserId(message.getReceiver())
                .title(userService.getUserInfo(message.getSender()).getUserName())
                .body(message.getMessage())
                .data(data)
                .build();
        noticeService.sendNotificationByToken(noticeRequestDto);
    }
}