package com.moijo.gomatch.app.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic으로 시작하는 메시지를 구독자에게 브로드캐스트
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");  // 클라이언트가 메시지를 보낼 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS 엔드포인트를 등록 (클라이언트가 /ws로 연결)
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
