package com.fashionflow.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*  */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //WebSocket 또는 SockJS 클라이언트가 웹소켓 핸드세이크 커넥션을 생성할 경로
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://localhost:8094")
                .withSockJS();
    }

    /* 채팅 경로 지정 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }
}
