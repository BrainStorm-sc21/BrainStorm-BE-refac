package com.brainstrom.meokjang.chat;

import com.brainstrom.meokjang.chat.dto.ChatMessageDto;
import com.brainstrom.meokjang.chat.dto.ChatRoomDto;
import com.brainstrom.meokjang.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final ChatService service;

    private WebSocketSession beforeSession;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessageDto chatMessage = mapper.readValue(payload, ChatMessageDto.class);
        log.info("session {}", chatMessage.toString());

        ChatRoomDto room = service.findRoomById(chatMessage.getRoomId());
        log.info("room {}", room.toString());

        room.handleAction(beforeSession, session, chatMessage, service);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("afterConnectionEstablished session {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        beforeSession = session;
        log.info("afterConnectionClosed session {}", beforeSession);
    }
}