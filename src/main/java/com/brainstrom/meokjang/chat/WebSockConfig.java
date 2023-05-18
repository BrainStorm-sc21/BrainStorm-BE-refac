package com.brainstrom.meokjang.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSockConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
<<<<<<< HEAD
        registry.addHandler(webSocketHandler, "ws/meokjang").setAllowedOrigins("*");
=======
        registry.addHandler(webSocketHandler, "/ws").setAllowedOrigins("*");
>>>>>>> feature_chat
    }
}