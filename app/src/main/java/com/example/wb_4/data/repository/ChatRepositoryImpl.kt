package com.example.wb_4.data.repository

import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.data.storage.MessageData
import com.example.wb_4.data.utils.toDomain
import com.example.wb_4.domain.model.MessageDomain
import com.example.wb_4.domain.repository.ChatRepository

class ChatRepositoryImpl : ChatRepository {
    override suspend fun getChatMessages(userId: Int): List<MessageDomain> {
        for (messages in CompanionUserDB.messageLists){
            if(messages.id == userId){
                return messages.messages.map { it.toDomain() }
            }
        }
        return emptyList()
    }
}