package com.example.wb_4.domain.usecase

import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.repository.DialogRepository
import javax.inject.Inject

class GetDialogListUseCase @Inject constructor(private val repository: DialogRepository) {

    suspend fun execute(lastId: Int): List<CompanionUserDomain>{
        return repository.getDialogs(lastId)
    }

}