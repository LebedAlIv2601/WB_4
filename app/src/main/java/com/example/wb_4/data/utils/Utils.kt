package com.example.wb_4.data.utils

import com.cesarferreira.tempo.*
import com.example.wb_4.data.storage.ChatConstants.AVATARS_SRC_LIST
import com.example.wb_4.data.storage.ChatConstants.FIRST_NAMES_LIST
import com.example.wb_4.data.storage.ChatConstants.MESSAGE_STRING_LIST
import com.example.wb_4.data.storage.ChatConstants.NOT_YOUR_MESSAGE
import com.example.wb_4.data.storage.ChatConstants.SECOND_NAMES_LIST
import com.example.wb_4.data.storage.ChatConstants.YOUR_MESSAGE
import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.data.storage.CompanionUserData
import com.example.wb_4.data.storage.MessageData
import com.example.wb_4.data.storage.MessageList
import com.example.wb_4.domain.model.CompanionUserDomain
import com.example.wb_4.domain.model.MessageDomain


//Функция создания нового случайного диалога со случайным набором сообщений
fun createRandomCompanionUserData(): CompanionUserData {

    //Новый список сообщений
    val newMessages = mutableListOf<MessageData>()

    //Рандомное число непрочитанных сообщений
    val unreadMessagesCountRandom = (0..12).random()
    //Рандомное количество сообщений
    val messagesCount = (22..42).random()
    //переменная для указания на автороство последнего сообщения - сам пользователь или собеседник
    val lastMessageSender = (NOT_YOUR_MESSAGE..YOUR_MESSAGE).random()

    //Добавление новых рандомных сообщений в список сообщений
    for (i in 0..messagesCount) {
        newMessages.add(
            MessageData(
                id = i,
                message = MESSAGE_STRING_LIST[(MESSAGE_STRING_LIST.indices).random()],
                isRead = i < messagesCount - unreadMessagesCountRandom,
                isYour = if (i < messagesCount - unreadMessagesCountRandom)
                    (NOT_YOUR_MESSAGE..YOUR_MESSAGE).random() == YOUR_MESSAGE else
                    lastMessageSender == YOUR_MESSAGE
            )
        )
    }

    val randomId = if (CompanionUserDB.messageLists.isNotEmpty())
        CompanionUserDB.messageLists[CompanionUserDB.messageLists.size - 1].id + 1 else 0

    //Добавления списка сообщений в общий список сообщений для каждого диалога
    CompanionUserDB.messageLists.add(
        MessageList(
            id = randomId,
            messages = newMessages
        )
    )


    //Возврат нового рандомного диалога со случайными данными
    return CompanionUserData(
        id = randomId,
        name = "${FIRST_NAMES_LIST[(FIRST_NAMES_LIST.indices).random()]} " +
                SECOND_NAMES_LIST[(SECOND_NAMES_LIST.indices).random()],
        avatar = AVATARS_SRC_LIST[(AVATARS_SRC_LIST.indices).random()],
        lastMessage = newMessages[newMessages.size - 1],
        lastMessageTime = Tempo.now - (0..8).random().hour - (0..59).random().minute,
        receivedUnreadMessagesCount = unreadMessagesCountRandom
    )
}

//Маппер CompanionUserData в CompanionUserDomain
fun CompanionUserData.toDomain(): CompanionUserDomain {
    return CompanionUserDomain(
        id = id,
        name = name,
        avatar = avatar,
        lastMessage = lastMessage?.toDomain(),
        lastMessageTime = lastMessageTime,
        receivedUnreadMessagesCount = receivedUnreadMessagesCount
    )
}

//Маппер MessageData в MessageDomain
fun MessageData.toDomain(): MessageDomain {
    return MessageDomain(
        id = id,
        message = message,
        isRead = isRead,
        isYour = isYour
    )
}

//Создание случайных изменений в списке диалогов
fun createRandomChanges() {

    //выбор одного или двух случайных диалогов, от которых придет новое сообщение
    for (i in 0..(0..1).random()) {
        val index = (CompanionUserDB.dialogsList.indices).random()
        val id = CompanionUserDB.dialogsList[index].id
        for(messageList in CompanionUserDB.messageLists) {
            if (messageList.id == id){
                val newMessage = MessageData(
                    id = messageList.messages.size,
                    message = MESSAGE_STRING_LIST[(MESSAGE_STRING_LIST.indices).random()],
                    isRead = false,
                    isYour = false
                )
                messageList.messages.add(newMessage)
                if(CompanionUserDB.dialogsList[index].lastMessage?.isYour == true){
                    CompanionUserDB.dialogsList[index].receivedUnreadMessagesCount = 1
                } else {
                    CompanionUserDB.dialogsList[index].receivedUnreadMessagesCount =
                        CompanionUserDB.dialogsList[index].receivedUnreadMessagesCount?.plus(1)
                }
                CompanionUserDB.dialogsList[index].lastMessage = newMessage
                CompanionUserDB.dialogsList[index].lastMessageTime = Tempo.now
            }
        }
    }

}

fun changeMessages(userId: Int){
    for (messageList in CompanionUserDB.messageLists){
        if(messageList.id == userId){
            for (i in 0..(0..1).random()) {
                messageList.messages[(messageList.messages.indices).random()].message =
                    MESSAGE_STRING_LIST[(MESSAGE_STRING_LIST.indices).random()]
            }

            messageList.messages.add(
                MessageData(
                    id = messageList.messages[messageList.messages.size-1].id + 1,
                    message = MESSAGE_STRING_LIST[(MESSAGE_STRING_LIST.indices).random()],
                    isRead = true,
                    isYour = false
                )
            )
            for(dialog in CompanionUserDB.dialogsList){
                if(dialog.id == userId){
                    dialog.lastMessage = messageList.messages[messageList.messages.size-1]
                    dialog.lastMessageTime = Tempo.now
                    dialog.receivedUnreadMessagesCount = 0
                }
            }
        }
    }


}


