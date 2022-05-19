package com.example.wb_4.presentation.dialog_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.wb_4.Resource
import com.example.wb_4.domain.usecase.GetDialogListUseCase
import kotlinx.coroutines.Dispatchers

class DialogListViewModel (private val getDialogUseCase: GetDialogListUseCase): ViewModel() {


    fun updateDialogList() = liveData(Dispatchers.IO){
        emit(Resource.Loading(data = null))
        try{
            Log.e("Loading", "Trying to load data from vm")
            emit(Resource.Success(data = getDialogUseCase.execute()))
            Log.e("Loading", "Data loaded")
        } catch(e: Exception){
            emit(Resource.Error(data = null, message = e.message ?: "Error Occurred!!!"))
        }
    }

}