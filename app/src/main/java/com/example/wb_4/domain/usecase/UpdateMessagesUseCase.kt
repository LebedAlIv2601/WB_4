package com.example.wb_4.domain.usecase

import com.example.wb_4.domain.model.MessageDomain
import com.example.wb_4.domain.repository.ChatRepository
import javax.inject.Inject

class UpdateMessagesUseCase @Inject constructor(private val repository: ChatRepository) {

    suspend fun execute(userId: Int): List<MessageDomain>{
        return repository.updateChatMessages(userId)
    }
}