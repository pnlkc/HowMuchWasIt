package com.example.howmuchwasit.data

import android.content.Context

/**
 * Hilt 사용으로 인한 사용 안함
 * 대신 AppModule 클래스 사용
 */

//interface AppContainer {
//    val itemRepository: ItemRepository
//}
//
//class DefaultAppContainer(private val context: Context) : AppContainer {
//    override val itemRepository: ItemRepository by lazy {
//        DefaultItemRepository(HowMuchWasItDatabase.getDatabase(context).itemDao())
//    }
//}