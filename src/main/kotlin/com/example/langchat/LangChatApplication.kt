package com.example.langchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LangChatApplication

fun main(args: Array<String>) {
    runApplication<LangChatApplication>(*args)
}
