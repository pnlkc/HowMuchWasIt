package com.example.howmuchwasit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HowMuchWasItApplication: Application() {
    /**
     * Hilt 사용으로 인해 사용 중지
     * 아래 코드는 AppContainer의 인스턴스는 필요 없어짐
     */

//    lateinit var container: AppContainer
//
//    override fun onCreate() {
//        super.onCreate()
//        container = DefaultAppContainer(this)
//    }
}