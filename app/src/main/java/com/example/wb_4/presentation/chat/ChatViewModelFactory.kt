package com.example.wb_4.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wb_4.domain.usecase.GetMessagesUseCase
import javax.inject.Inject

class ChatViewModelFactory @Inject constructor(private val getMessagesUseCase: GetMessagesUseCase) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == ChatViewModel::class.java)
        return ChatViewModel(getMessagesUseCase) as T
    }
}