package com.example.langchat.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RedirectDto (
    val code: String?,

    val error: String?,

    @JsonProperty("error_description")
    val errorDescription: String?,

    val state: String?
)