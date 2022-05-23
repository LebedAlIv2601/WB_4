package com.example.wb_4.domain.repository

import com.example.wb_4.domain.model.MessageDomain

interface ChatRepository {

    suspend fun getChatMessages(userId: Int): List<MessageDomain>

}