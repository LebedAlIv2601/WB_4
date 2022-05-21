package com.example.wb_4.data.storage


import java.util.*

data class CompanionUserData(
    val id: Int,
    val name: String,
    val avatar: String?,
    var lastMessage: MessageData?,
    var lastMessageTime: Date?,
    var receivedUnreadMessagesCount: Int?
)
