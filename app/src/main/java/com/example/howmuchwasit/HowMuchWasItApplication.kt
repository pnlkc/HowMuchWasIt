package com.example.howmuchwasit

import android.app.Application
import com.example.howmuchwasit.data.AppContainer
import com.example.howmuchwasit.data.DefaultAppContainer

class HowMuchWasItApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}