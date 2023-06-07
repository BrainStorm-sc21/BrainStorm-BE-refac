package com.brainstrom.meokjang.chat.repository;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findByRoomId(String roomId);
    List<ChatRoom> findAll();
    ChatRoom save(ChatRoom chatRoom);
}
