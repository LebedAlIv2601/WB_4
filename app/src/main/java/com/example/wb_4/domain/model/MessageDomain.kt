package com.example.wb_4.domain.model

data class MessageDomain(
    val id: Int,
    val message: String,
    val isRead: Boolean,
    val isYour: Boolean
)
