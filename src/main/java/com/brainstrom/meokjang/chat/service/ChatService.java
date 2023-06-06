package com.brainstrom.meokjang.chat.service;


import com.brainstrom.meokjang.chat.domain.ChatMessage;
import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.chat.dto.*;
import com.brainstrom.meokjang.chat.repository.ChatMessageRepository;
import com.brainstrom.meokjang.chat.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {
    private final ObjectMapper mapper;
    private Map<String, ChatRoomDto> chatRooms;
    private ChatRoomRepository chatRepo;
    private ChatMessageRepository messageRepo;
    @Autowired
    public ChatService(ObjectMapper mapper, ChatRoomRepository chatRepo, ChatMessageRepository messageRepo) {
        this.mapper = mapper;
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
    }

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomResponse> findAllRoom(){
        return chatRepo.findAll().stream().map(ChatRoomResponse::new).toList();
    }

    public ChatRoom findByRoomId(String roomId){
        return chatRepo.findByRoomId(roomId);
    }

    public ChatRoomDto findRoomById(String roomId){
        return chatRooms.get(roomId);
    }

    public ChatRoomResponse createRoom(ChatRoomRequest chatRoomRequest) {
        String roomId = UUID.randomUUID().toString();

        ChatRoomDto roomDto = ChatRoomDto.builder()
                .roomId(roomId)
                .sender(chatRoomRequest.getSender())
                .receiver(chatRoomRequest.getReceiver())
                .build();

        chatRooms.put(roomId, roomDto);
        ChatRoom room = ChatRoom.toEntity(roomDto);
        return new ChatRoomResponse(chatRepo.save(room));
    }

    public ChatMessage saveMessage(ChatMessage chatMessage){
        return messageRepo.save(chatMessage);
    }

    public List<ChatMessageResponse> findAllMessageById(Long id){
        return messageRepo.findByChatRoom_Id(id).stream().map(ChatMessageResponse::new).toList();
    }

    public List<ChatRoomResponse> findRoomByUserId(Long userId){
        return chatRepo.findByUserId(userId).stream().map(ChatRoomResponse::new).toList();
    }

    public void sendMessage(WebSocketSession session, ChatMessageDto message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}