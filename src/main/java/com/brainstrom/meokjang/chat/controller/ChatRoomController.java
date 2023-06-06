package com.brainstrom.meokjang.chat.controller;

import com.brainstrom.meokjang.chat.domain.ChatMessage;
import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.chat.dto.ChatMessageResponse;
import com.brainstrom.meokjang.chat.dto.ChatRoomRequest;
import com.brainstrom.meokjang.chat.dto.ChatRoomResponse;
import com.brainstrom.meokjang.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService service;
    @Autowired
    public ChatRoomController(ChatService service) {
        this.service = service;
    }

    @PostMapping
    public ChatRoomResponse createRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        return service.createRoom(chatRoomRequest);
    }

    @GetMapping("/room/{userId}")
    public List<ChatRoomResponse> findRoomByUserId(@PathVariable Long userId){
        return service.findRoomByUserId(userId);
    }

    @GetMapping("/room")
    public List<ChatRoomResponse> findAllRooms(){
        return service.findAllRoom();
    }


    @GetMapping("/message/{roomId}")
    public List<ChatMessageResponse> findAllMessageById(@PathVariable Long roomId){
        return service.findAllMessageById(roomId);
    }
}