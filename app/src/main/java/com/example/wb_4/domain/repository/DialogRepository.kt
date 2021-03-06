package com.example.wb_4.domain.repository

import com.example.wb_4.domain.model.CompanionUserDomain

interface DialogRepository {

    suspend fun getDialogs(lastId: Int): List<CompanionUserDomain>

    suspend fun updateDialogs(): List<CompanionUserDomain>

}