package com.example.wb_4.data.storage


import java.util.*

data class CompanionUserData(
    val id: Int,
    val name: String,
    val avatar: String?,
    val lastMessage: MessageData?,
    val lastMessageTime: Date?,
    val receivedUnreadMessagesCount: Int?
)
