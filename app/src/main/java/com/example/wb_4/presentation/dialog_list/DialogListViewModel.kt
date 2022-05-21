package com.example.wb_4.presentation.dialog_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.wb_4.presentation.utils.Resource
import com.example.wb_4.domain.usecase.GetDialogListUseCase
import com.example.wb_4.domain.usecase.UpdateDialogListUseCase
import kotlinx.coroutines.Dispatchers

class DialogListViewModel(
    private val getDialogUseCase: GetDialogListUseCase,
    private val updateUseCase: UpdateDialogListUseCase
) : ViewModel() {


    fun getDialogList(lastId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(data = null))
        try {
            Log.e("Loading", "Trying to load data from with $lastId")
            emit(Resource.Success(data = getDialogUseCase.execute(lastId)))
            Log.e("Loading", "Data loaded")
        } catch (e: Exception) {
            emit(Resource.Error(data = null, message = e.message ?: "Error Occurred!!!"))
        }
    }

    fun updateDialogList() = liveData(Dispatchers.IO){
        emit(Resource.Loading(data = null))
        try {
            Log.e("Loading", "Trying to load data from vm")
            emit(Resource.Success(data = updateUseCase.execute()))
            Log.e("Loading", "Data loaded")
        } catch (e: Exception) {
            emit(Resource.Error(data = null, message = e.message ?: "Error Occurred!!!"))
        }
    }


}