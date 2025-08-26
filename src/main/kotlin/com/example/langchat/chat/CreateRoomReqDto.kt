package com.example.langchat.chat

data class CreateRoomReqDto (
    val roomName: String?, // 채팅방 이름
    val participants: List<String>
){
}