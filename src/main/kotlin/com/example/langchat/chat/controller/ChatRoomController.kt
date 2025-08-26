package com.example.langchat.chat.controller

import com.example.langchat.chat.CreateRoomReqDto
import com.example.langchat.util.IdGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 채팅방 관련 API 엔드포인트
 * 채팅방 생성, 조회, 삭제 등의 기능을 구현할 수 있습니다.
 */

@RestController
@RequestMapping("/api/chat")
class ChatRoomController {

    /**
     * 채팅방 생성 API
     * @param createRoomReqDto 채팅방 생성 요청 DTO
     */
    @PostMapping("/create")
    fun createChatRoom(createRoomReqDto: CreateRoomReqDto) {
        val roomId = IdGenerator.generateId()
        // 새롭게 채팅방을 생성하고 (uuid 로 식별)
        // 채팅방 정보, 참여자 목록 등을 데이터베이스에 저장한다.
        // 이후에 채팅 정보를 클라이언트에게 반환한다. (바로 생성된 채팅방으로 이동할 수 있도록)
    }

    /**
     * 채팅방 목록 조회 API
     * @return 채팅방 목록
     */
    @GetMapping("/rooms")
    fun getChatRooms() {
        // 실제 채팅방 데이터를 담고 있는 dto List 반환한다.
    }


    /**
     * 채팅방 조회 API
     * @param roomId 조회할 채팅방 ID
     */
    @GetMapping("/rooms/{roomId}")
    fun getChatRoom(roomId: String) {
        // 특정 채팅방의 정보를 반환한다.
        // 채팅방에 참여한 사용자 목록, 채팅 메시지 목록 등을 포함할 수 있다.
    }

}