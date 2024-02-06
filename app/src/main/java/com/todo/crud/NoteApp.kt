package com.todo.crud

import android.app.Application
import com.todo.crud.di.appModule
import com.todo.crud.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.mp.KoinPlatform.startKoin

class NoteApp: Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@NoteApp)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, viewModule))
        }
    }

}