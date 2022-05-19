package com.example.wb_4.data.repository

import android.util.Log
import com.example.wb_4.data.createRandomCompanionUserData
import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.data.toDomain
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.repository.DialogRepository
import javax.inject.Inject

class DialogRepositoryImpl : DialogRepository {

    override suspend fun getDialogs(): List<CompanionUserDomain> {
        Log.e("GetDialogs", "Getting Dialogs")
        for (i in 0..20){
            Log.e("Creating", "Creating new dialogs")
            CompanionUserDB.dialogsList.add(createRandomCompanionUserData())
        }
        CompanionUserDB.dialogsList.sortBy { it.lastMessageTime }
        return CompanionUserDB.dialogsList.map { it.toDomain() }
    }
}