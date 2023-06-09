package com.brainstrom.meokjang.chat.repository;

import com.brainstrom.meokjang.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findById(Long id);

    void deleteById(Long id);

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findByChatRoom_Id(Long id);
}
