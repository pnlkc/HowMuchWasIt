package com.example.howmuchwasit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val name: String,
    val price: Int,
    val quantity: Int,
)