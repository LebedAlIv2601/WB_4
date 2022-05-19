package com.example.wb_4.data

import android.os.Message
import com.cesarferreira.tempo.Tempo
import com.cesarferreira.tempo.hour
import com.cesarferreira.tempo.minus
import com.cesarferreira.tempo.minute
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


fun createRandomCompanionUserData(): CompanionUserData {

    val newMessages = mutableListOf<MessageData>()

    val unreadMessagesCountRandom = (0..12).random()
    val messagesCount = (12..42).random()
    val lastMessageSender = (NOT_YOUR_MESSAGE..YOUR_MESSAGE).random()

    for (i in 0..messagesCount){
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

        CompanionUserDB.messageLists.add(
            MessageList(
                id = if(CompanionUserDB.messageLists.isNotEmpty())
                    CompanionUserDB.dialogsList[CompanionUserDB.dialogsList.size - 1].id + 1 else 0,
                messages = newMessages
            )
        )



    return CompanionUserData(
        id = if(CompanionUserDB.dialogsList.isNotEmpty())
            CompanionUserDB.dialogsList[CompanionUserDB.dialogsList.size - 1].id + 1 else 0,
        name = "${FIRST_NAMES_LIST[(FIRST_NAMES_LIST.indices).random()]} " +
                SECOND_NAMES_LIST[(SECOND_NAMES_LIST.indices).random()],
        avatar = AVATARS_SRC_LIST[(AVATARS_SRC_LIST.indices).random()],
        lastMessage = newMessages[newMessages.size - 1],
        lastMessageTime = Tempo.now - (0..8).random().hour - (0..59).random().minute,
        receivedUnreadMessagesCount = unreadMessagesCountRandom)
}

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

fun MessageData.toDomain(): MessageDomain{
    return MessageDomain(
        id = id,
        message = message,
        isRead = isRead,
        isYour = isYour
    )
}
