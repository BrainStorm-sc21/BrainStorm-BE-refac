package com.brainstrom.meokjang.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestBody String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }

    @GetMapping("/{roomId}")
    public ChatRoom findRoomById(@PathVariable String roomId) {
        return chatService.findRoomById(roomId);
    }

    @GetMapping("/{userId}")
    public ChatRoom findRoomByUserId(@PathVariable String userId) {
        return chatService.findRoomByUserId(userId);
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        chatService.deleteRoom(roomId);
    }
}
