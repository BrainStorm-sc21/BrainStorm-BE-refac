package com.brainstrom.meokjang.chat.repository;

import com.brainstrom.meokjang.chat.dto.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ChatRepository extends JpaRepository<ChatRoom, String> {

    // 전체 채팅방 조회
    public List<ChatRoom> findAll();

    // roomID 기준으로 채팅방 찾기
    public Optional<ChatRoom> findById(String roomId);

    // 채팅방 생성
    public ChatRoom save(String roomName);

    // 채팅방 삭제
    public void deleteById(String roomId);


}

