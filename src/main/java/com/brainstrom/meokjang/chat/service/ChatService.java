package com.brainstrom.meokjang.chat.service;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import com.brainstrom.meokjang.chat.dto.ChatRoomDto;
import com.brainstrom.meokjang.chat.repository.ChatRoomRepository;
import com.brainstrom.meokjang.food.dto.response.FoodResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class ChatService {

    private ChatRoomRepository chatRoomRepo;
    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    //채팅방 불러오기
    public List<ChatRoomDto> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> rooms = chatRoomRepo.findAll();
        List<ChatRoomDto> result = rooms.stream().map(ChatRoomDto::new).toList();
        Collections.reverse(result);

        return result;
    }

//    public List<ChatRoomDto> findAllByUserId(Long userId) {
//        //채팅방 최근 생성 순으로 반환
//        List<ChatRoomDto> result = chatRoomRepo.findAllByUserId(userId);
//        Collections.reverse(result);
//
//        return result;
//    }

    //채팅방 하나 불러오기
    public ChatRoomDto findById(String roomId) {
        ChatRoom chatRoom = chatRoomRepo.findByRoomId(roomId);
        return new ChatRoomDto(chatRoom);
    }

    //채팅방 생성
    public ChatRoomDto createRoom(ChatRoomDto chatRoomDto) {
        chatRoomRepo.save(chatRoomDto.toEntity());
        return chatRoomDto;
    }
}