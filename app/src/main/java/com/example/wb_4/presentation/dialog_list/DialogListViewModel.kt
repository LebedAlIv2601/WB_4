package com.example.wb_4.presentation.dialog_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.usecase.GetDialogListUseCase
import com.example.wb_4.domain.usecase.UpdateDialogListUseCase
import kotlinx.coroutines.*

class DialogListViewModel(
    private val getDialogUseCase: GetDialogListUseCase,
    private val updateUseCase: UpdateDialogListUseCase
) : ViewModel() {

    private val _dialogsList = MutableLiveData<List<CompanionUserDomain>>()
    val dialogsList: LiveData<List<CompanionUserDomain>>
        get() = _dialogsList

    private val _loadPermission = MutableLiveData<Boolean>()
    val loadPermission: LiveData<Boolean>
        get() = _loadPermission

    private val _dataLastIndex = MutableLiveData<Int>()
    val dataLastIndex: LiveData<Int>
        get() = _dataLastIndex


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getDialogs(0)
        _loadPermission.value = true
        _dataLastIndex.value = 0
    }

    fun getDialogs(lastId: Int){
        uiScope.launch {
            val newList = getDialogsUseCaseExecuting(lastId)
            if(newList.isNotEmpty()){
                val prevList = _dialogsList.value
                val resultList = mutableListOf<CompanionUserDomain>()
                prevList?.let { resultList.addAll(it) }
                resultList.addAll(newList)
                _dialogsList.value = resultList
            } else {
                _loadPermission.value = false
            }
        }
    }

    private suspend fun getDialogsUseCaseExecuting(lastId: Int): List<CompanionUserDomain>{
        return withContext(Dispatchers.IO){
            getDialogUseCase.execute(lastId)
        }
    }

    fun updateDialogs(){
        uiScope.launch {
            val newList = updateDialogsUseCaseExecuting()
            _dialogsList.value = newList
            _loadPermission.value = true
            _dataLastIndex.value = 0
        }
    }

    private suspend fun updateDialogsUseCaseExecuting(): List<CompanionUserDomain>{
        return withContext(Dispatchers.IO){
            updateUseCase.execute()
        }
    }

    fun setupDataLastIndex(index: Int){
        _dataLastIndex.value = index
    }

}