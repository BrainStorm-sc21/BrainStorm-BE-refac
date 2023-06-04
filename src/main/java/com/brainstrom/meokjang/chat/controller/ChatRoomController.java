package com.brainstrom.meokjang.chat.controller;

import com.brainstrom.meokjang.chat.dto.ChatRoomDto;
import com.brainstrom.meokjang.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;
    @Autowired
    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomDto> room() {
        return chatService.findAllRoom();
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoomDto createRoom(@RequestBody ChatRoomDto chatRoomDto) {
        return chatService.createRoom(chatRoomDto);
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoomDto roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}