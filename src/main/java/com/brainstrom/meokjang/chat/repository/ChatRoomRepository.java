package com.brainstrom.meokjang.chat.repository;

import com.brainstrom.meokjang.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query(value = "select * from ChatRoom where sender = :id or receiver = :id", nativeQuery = true)

    List<ChatRoom> findByUserId(@Param(value = "id") Long id);
    List<ChatRoom> findAll();
    ChatRoom findByRoomId(String roomId);
    ChatRoom save(ChatRoom chatRoom);
}
