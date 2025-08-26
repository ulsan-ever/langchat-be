package com.example.langchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class LangChatApplication

fun main(args: Array<String>) {
    runApplication<LangChatApplication>(*args)
}
