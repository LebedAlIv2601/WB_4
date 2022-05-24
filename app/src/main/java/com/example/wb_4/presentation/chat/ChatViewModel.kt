package com.example.wb_4.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.model.MessageDomain
import com.example.wb_4.domain.usecase.GetMessagesUseCase
import kotlinx.coroutines.*

class ChatViewModel (private val getMessagesUseCase: GetMessagesUseCase) : ViewModel() {

    private val _messagesList = MutableLiveData<List<MessageDomain>>()
    val messagesList: LiveData<List<MessageDomain>>
        get() = _messagesList

    private val _loadPermission = MutableLiveData<Boolean>()
    val loadPermission: LiveData<Boolean>
        get() = _loadPermission

    private val _dataLastIndex = MutableLiveData<Int>()
    val dataLastIndex: LiveData<Int>
        get() = _dataLastIndex

    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _loadPermission.value = true
        _dataLastIndex.value = -1
    }

    fun getMessages(userId: Int, lastId: Int){
        ioScope.launch {
            val newList = getMessagesUseCaseExecuting(userId, lastId)
            if(newList.isNotEmpty()){
                val prevList = _messagesList.value
                val resultList = mutableListOf<MessageDomain>()
                prevList?.let { resultList.addAll(it) }
                resultList.addAll(newList)
                _messagesList.value = resultList
            } else {
                _loadPermission.value = false
            }
        }
    }

    private suspend fun getMessagesUseCaseExecuting(userId: Int, lastId: Int): List<MessageDomain>{
        return withContext(Dispatchers.IO){
            getMessagesUseCase.execute(userId, lastId)
        }
    }

    fun setupDataLastIndex(index: Int){
        _dataLastIndex.value = index
    }

}