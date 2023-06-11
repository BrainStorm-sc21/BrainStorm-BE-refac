package com.brainstrom.meokjang.chat.controller;

import com.brainstrom.meokjang.chat.dto.ChatMessageResponse;
import com.brainstrom.meokjang.chat.dto.ChatRoomRequest;
import com.brainstrom.meokjang.chat.dto.ChatRoomResponse;
import com.brainstrom.meokjang.chat.service.ChatService;
import com.brainstrom.meokjang.common.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> createRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        ChatRoomResponse result = service.createRoom(chatRoomRequest);
        ApiResponse apiResponse = new ApiResponse(200, "채팅방 생성 성공", result);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/room/{userId}")
    public ResponseEntity<ApiResponse> findRoomByUserId(@PathVariable Long userId){
        List<ChatRoomResponse> result = service.findRoomByUserId(userId);
        ApiResponse apiResponse = new ApiResponse(200, "유저 채팅방 조회 성공", result);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse> DeleteById(@PathVariable Long roomId){
        ApiResponse apiResponse = null;
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/room")
    public ResponseEntity<ApiResponse> findAllRooms(){
        List<ChatRoomResponse> result = service.findAllRoom();
        ApiResponse apiResponse = new ApiResponse(200, "전체 채팅방 조회 성공", result);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/message/{roomId}")
    public ResponseEntity<ApiResponse> findAllMessageById(@PathVariable Long roomId){
        List<ChatMessageResponse> result = service.findAllMessageById(roomId);
        ApiResponse apiResponse = new ApiResponse(200, "채팅방 메시지 조회 성공", result);
        return ResponseEntity.ok(apiResponse);
    }
}