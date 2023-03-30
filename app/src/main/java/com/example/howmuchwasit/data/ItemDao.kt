package com.example.howmuchwasit.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM products WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM products ORDER BY date DESC, name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM products WHERE REPLACE(date, ' / ', '-') > DATE('now', '-3 months') ORDER BY date DESC, name ASC")
    fun getRecentItem(): Flow<List<Item>>
}