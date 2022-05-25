package com.example.wb_4.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wb_4.domain.usecase.GetMessagesUseCase
import com.example.wb_4.domain.usecase.UpdateMessagesUseCase
import javax.inject.Inject

class ChatViewModelFactory @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val updateMessagesUseCase: UpdateMessagesUseCase
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == ChatViewModel::class.java)
        return ChatViewModel(getMessagesUseCase, updateMessagesUseCase) as T
    }
}