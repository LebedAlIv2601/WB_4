package com.example.wb_4.presentation.dialog_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wb_4.domain.usecase.GetDialogListUseCase
import com.example.wb_4.domain.usecase.UpdateDialogListUseCase
import javax.inject.Inject

class DialogListViewModelFactory @Inject constructor(
    private val getDialogUseCase: GetDialogListUseCase,
    private val updateUseCase: UpdateDialogListUseCase
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == DialogListViewModel::class.java)
        return DialogListViewModel(getDialogUseCase, updateUseCase) as T
    }
}