package com.example.wb_4.data.repository

import android.util.Log
import com.example.wb_4.data.storage.ChatConstants.LOAD_SIZE
import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.data.storage.MessageData
import com.example.wb_4.data.utils.toDomain
import com.example.wb_4.domain.model.MessageDomain
import com.example.wb_4.domain.repository.ChatRepository

class ChatRepositoryImpl : ChatRepository {
    override suspend fun getChatMessages(userId: Int, lastId: Int): List<MessageDomain> {
        val partList = mutableListOf<MessageDomain>()
        for (messages in CompanionUserDB.messageLists) {
            if (messages.id == userId) {
                if (lastId == -1) {
                    Log.e("START", "START")
                    for (i in messages.messages.size-1 downTo messages.messages.size - LOAD_SIZE) {
                        partList.add(messages.messages[i].toDomain())
                    }
                    return partList
                } else if (lastId == messages.messages.size-1) {
                    // Если это конец списка, то возвращаем пустой лист
                    return emptyList()
                } else {
                    //Если количество оставшихся элементов меньше размера LOAD_SIZE, то выдаем оставшиеся данные
                    //Иначе выдаем следующие 10 элементов
                    Log.e("DATA", "Data loaded")
                    for (i in 1..(if (lastId > messages.messages.size - 1 - LOAD_SIZE)
                        messages.messages.size-1-lastId else LOAD_SIZE)) {
                        partList.add(messages.messages[messages.messages.size - 1 - lastId - i].toDomain())
                    }

                    return partList
                }
            }
        }
        return emptyList()
    }
}