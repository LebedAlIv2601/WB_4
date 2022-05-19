package com.example.wb_4.domain.model

import java.util.*

data class CompanionUserDomain(
    val id: Int,
    val name: String,
    val avatar: String?,
    val lastMessage: MessageDomain?,
    val lastMessageTime: Date?,
    val receivedUnreadMessagesCount: Int?
)
