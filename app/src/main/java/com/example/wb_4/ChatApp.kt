package com.example.wb_4

import android.app.Application
import android.content.Context
import com.example.wb_4.di.AppComponent
import com.example.wb_4.di.DaggerAppComponent

class ChatApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}

val Context.appComponent: AppComponent
    get() = when(this){
        is ChatApp -> appComponent
        else -> applicationContext.appComponent
    }