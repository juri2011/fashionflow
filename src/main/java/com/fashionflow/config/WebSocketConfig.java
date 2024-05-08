package com.fashionflow.config;

import com.fashionflow.handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        
        //CORS : 현재 실행중인 웹 어플리케이션이 다른 출처의 선택한 자원에 접근할 수 있는 권한 부여
        registry.addHandler(chatHandler, "ws/chat")
                .setAllowedOrigins("http://localhost:8094");
    }
}
