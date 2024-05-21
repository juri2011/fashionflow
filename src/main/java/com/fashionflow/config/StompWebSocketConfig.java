package com.fashionflow.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /* STOMP 엔드포인트를 등록 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 또는 SockJS 클라이언트가 웹소켓 핸드셰이크 커넥션을 생성할 경로
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://localhost:8094") // 지정된 오리진만 허용
                .withSockJS(); // SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 사용 가능하게 설정
    }

    /* 채팅 경로 지정 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 메시지를 발행할 때 사용하는 경로
        registry.setApplicationDestinationPrefixes("/pub");

        // 메시지를 구독할 때 사용하는 경로
        registry.enableSimpleBroker("/sub");
    }
}
