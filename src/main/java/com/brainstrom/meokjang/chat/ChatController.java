package com.brainstrom.meokjang.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name){
        return service.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRooms(){
        return service.findAllRoom();
    }

    @GetMapping("/{roomId}")
    public ChatRoom findRoomById(@PathVariable String roomId){
        return service.findRoomById(roomId);
    }


}