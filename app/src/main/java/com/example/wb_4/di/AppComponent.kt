package com.example.wb_4.di

import com.example.wb_4.data.repository.ChatRepositoryImpl
import com.example.wb_4.data.repository.DialogRepositoryImpl
import com.example.wb_4.data.storage.CompanionUserDB
import com.example.wb_4.domain.repository.ChatRepository
import com.example.wb_4.domain.repository.DialogRepository
import com.example.wb_4.presentation.chat.ChatFragment
import com.example.wb_4.presentation.dialog_list.DialogListFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [DataModule::class, DomainBinds::class])
interface AppComponent {

    fun inject(dialogListFragment: DialogListFragment)
    fun inject(chatFragment: ChatFragment)

}

@Module
class DataModule{
    @Provides
    fun getDialogRepositoryImpl(): DialogRepositoryImpl{
        return DialogRepositoryImpl()
    }

    @Provides
    fun getChatRepositoryImpl(): ChatRepositoryImpl{
        return ChatRepositoryImpl()
    }
}

@Module
interface DomainBinds{

    @Binds
    fun dialogRepositoryImplToInterface(
        repositoryImpl: DialogRepositoryImpl
    ): DialogRepository

    @Binds
    fun chatRepositoryImplToInterface(
        repositoryImpl: ChatRepositoryImpl
    ): ChatRepository

}

