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

    @Query("SELECT * from products WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from products ORDER BY date DESC")
    fun getAllItems(): Flow<List<Item>>
}