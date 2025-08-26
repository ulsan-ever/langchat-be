package com.example.langchat.chat.controller

import com.example.langchat.chat.entity.ChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

/**
 * 채팅 메시지를 처리하는 컨트롤러 (WebSocket 엔드포인트)
 */

@Controller
class ChatController (
    private val messagingTemplate: SimpMessagingTemplate
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(@Payload chatMessage: ChatMessage) {
        // 채팅 내용은 RDB 에 저장해둔다.

        // 필요하면 알림 서비스 전송

        // 채팅 메시지를 특정 채팅방으로 전송
        val destination = "/topic/chat/room/${chatMessage.roomId}"
        messagingTemplate.convertAndSend(destination, chatMessage)
    }

    /**
     * 사용자가 채팅방에 입장했음을 알리는 엔드포인트 (선택적 구현)
     * "OO님이 입장하셨습니다." 와 같은 시스템 메시지를 보낼 때 사용 가능합니다.
     */
    @MessageMapping("/chat.enter")
    fun enterRoom(){

    }
}