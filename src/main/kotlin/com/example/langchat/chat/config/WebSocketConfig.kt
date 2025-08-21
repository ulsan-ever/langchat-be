package com.example.langchat.chat.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화
class WebSocketConfig : WebSocketMessageBrokerConfigurer{
    override fun registerStompEndpoints(registry : StompEndpointRegistry) {
        // 웹 소켓 연결 엔드포인트
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*")
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // "/app" 으로 시작하는 주소로 요청이 들어오면 @MessageMapping 어노테이션이 붙은 메소드로 라우팅
        registry.setApplicationDestinationPrefixes("/app")

        // "/topic" 으로 시작하는 주소를 구독한 클라이언트에게 메시지를 전달
        registry.enableSimpleBroker("/topic")
    }

}