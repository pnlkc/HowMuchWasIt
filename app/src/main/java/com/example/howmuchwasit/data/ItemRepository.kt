package com.example.howmuchwasit.data

import kotlinx.coroutines.flow.Flow
import java.nio.channels.FileLock

interface ItemRepository {
    suspend fun addItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

    fun getAllItemsNameStream(): Flow<List<String>>

    fun getItemsListStream(name: String): Flow<List<Item>>

    fun getLowestItemStream(name: String): Flow<Item>

    fun getItemStream(id: Int): Flow<Item>

    fun getRecentItemStream(): Flow<List<Item>>

    fun getSearchItemStream(searchTerm: String): Flow<List<String>>
}