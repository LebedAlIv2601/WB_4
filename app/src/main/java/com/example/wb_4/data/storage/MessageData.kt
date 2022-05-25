package com.example.wb_4.data.storage

data class MessageData(
    val id: Int,
    var message: String,
    val isRead: Boolean,
    val isYour: Boolean
)
