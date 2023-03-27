package com.example.howmuchwasit.data

import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun addItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

    fun getAllItemsStream(): Flow<List<Item>>

    fun getItemStream(id: Int): Flow<Item>
}