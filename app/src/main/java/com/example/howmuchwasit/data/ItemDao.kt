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

    // 바인딩 변수의 앞뒤에 || 연산자를 통해 '%' 붙여주어야 됨
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    fun getSearchItem(searchTerm: String) : Flow<List<Item>>
}