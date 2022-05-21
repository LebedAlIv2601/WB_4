package com.example.wb_4.data.repository

import android.util.Log
import com.example.wb_4.data.utils.createRandomChanges
import com.example.wb_4.data.utils.createRandomCompanionUserData
import com.example.wb_4.data.storage.ChatConstants.LOAD_SIZE
import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.data.utils.toDomain
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.repository.DialogRepository

class DialogRepositoryImpl : DialogRepository {

    //Метод получения списка диалогов, начиная с конкретного id
    override suspend fun getDialogs(lastId: Int): List<CompanionUserDomain> {
        Log.e("GetDialogs", "Getting Dialogs")

        //Список сообщений для возврата
        val partList = mutableListOf<CompanionUserDomain>()

        //Если это первый запрос для получения диалогов, то создаем 33 рандомных диалога и
        //выдаем первые 10
        if (lastId == 0) {
            for (i in 0..32) {
                Log.e("Creating", "Creating new dialogs")
                CompanionUserDB.dialogsList.add(createRandomCompanionUserData())
            }
            //Сортируем весь список диалогов по убыванию по дате
            CompanionUserDB.dialogsList.sortByDescending { it.lastMessageTime }
            for (i in 0 until LOAD_SIZE) {
                partList.add(CompanionUserDB.dialogsList[i].toDomain())
            }
        } else if (lastId == CompanionUserDB.dialogsList.size - 1) {
            // Если это конец списка, то возвращаем пустой лист
            return emptyList()
        } else {
            //Если количество оставшихся элементов меньше размера LOAD_SIZE, то выдаем оставшиеся данные
            //Иначе выдаем следующие 10 элементов
            if (lastId > CompanionUserDB.dialogsList.size - 1 - LOAD_SIZE) {
                for (i in 1..(CompanionUserDB.dialogsList.size - 1 - lastId)) {
                    partList.add(CompanionUserDB.dialogsList[lastId + i].toDomain())
                }
            } else {
                for (i in 1..LOAD_SIZE) {
                    partList.add(CompanionUserDB.dialogsList[lastId + i].toDomain())
                }
            }
        }

        Log.e("get", "Get dialogs from $lastId, size = ${CompanionUserDB.dialogsList.size}")

        return partList
    }

    //Метод обновления списка диалогов
    override suspend fun updateDialogs(): List<CompanionUserDomain> {
        //Создаем 4 новых случайных диалога
        for (i in 0..3) {
            Log.e("Creating", "Creating new dialogs")
            CompanionUserDB.dialogsList.add(createRandomCompanionUserData())
        }

        //Применяем рандомные изменения в списке диалогов
        createRandomChanges()

        //Сортируем в порядке убывания по дате весь список диалогов
        CompanionUserDB.dialogsList.sortByDescending { it.lastMessageTime }

        //Создаем новый список для возврата и заполняем его первыми 10 элементами
        val partList = mutableListOf<CompanionUserDomain>()

        for (i in 0 until LOAD_SIZE) {
            partList.add(CompanionUserDB.dialogsList[i].toDomain())
        }

        Log.e("update", "size = ${CompanionUserDB.dialogsList.size}")

        return partList
    }
}