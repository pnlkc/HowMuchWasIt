package com.example.howmuchwasit.data

import android.content.Context

interface AppContainer {
    val itemRepository: ItemRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val itemRepository: ItemRepository by lazy {
        DefaultItemRepository(HowMuchWasItDatabase.getDatabase(context).itemDao())
    }
}