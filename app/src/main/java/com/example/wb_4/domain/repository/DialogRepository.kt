package com.example.wb_4.domain.repository

import com.example.wb_4.domain.model.CompanionUserDomain

interface DialogRepository {

    suspend fun getDialogs(): List<CompanionUserDomain>

}