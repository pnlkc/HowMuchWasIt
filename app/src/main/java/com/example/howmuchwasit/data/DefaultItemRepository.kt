package com.example.howmuchwasit.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultItemRepository @Inject constructor(private val itemDao: ItemDao) : ItemRepository {
    override suspend fun addItem(item: Item) = itemDao.insert(item = item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item = item)

    override suspend fun updateItem(item: Item) = itemDao.update(item = item)

    override fun getAllItemsNameStream(): Flow<List<String>> = itemDao.getAllItemsName()

    override fun getItemsListStream(name: String): Flow<List<Item>> = itemDao.getItemsList(name)

    override fun getLowestItemStream(name: String): Flow<Item?> = itemDao.getLowestItem(name)

    override fun getItemStream(id: Int): Flow<Item> = itemDao.getItem(id)

    override fun getRecentItemStream(): Flow<List<Item>> = itemDao.getRecentItem()

    override fun getSearchItemStream(searchTerm: String): Flow<List<String>> =
        itemDao.getSearchItem(searchTerm)
}