package com.example.howmuchwasit.data

import kotlinx.coroutines.flow.Flow

class DefaultItemRepository(private val itemDao: ItemDao) : ItemRepository {
    override suspend fun addItem(item: Item) = itemDao.insert(item = item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item = item)

    override suspend fun updateItem(item: Item) = itemDao.update(item = item)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item> = itemDao.getItem(id)

    override fun getRecentItemStream(): Flow<List<Item>> = itemDao.getRecentItem()

    override fun getSearchItemStream(searchTerm: String): Flow<List<Item>> =
        itemDao.getSearchItem(searchTerm)
}