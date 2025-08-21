package com.example.langchat.chat.config

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

@Component
class WebSocketEventListener {
    private val log = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        log.info("WebSocket connected! Session ID: {}", event.message.headers["simpSessionId"])
    }

    @EventListener
    fun handleWebSocketSubscribeListener(event: SessionSubscribeEvent) {
        val destination = event.message.headers["simpDestination"]
        log.info("User subscribed to [{}]. Session ID: {}", destination, event.message.headers["simpSessionId"])
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        log.info("User disconnected. Session ID: {}", event.sessionId)
    }
}