package com.example.howmuchwasit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.howmuchwasit.ui.item.ItemUiState

@Entity(tableName = "products")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val name: String,
    val price: Int,
    val onePrice: Int,
    val quantity: Int,
)

// Room Entity 클래스를 ItemUiState로 변경
fun Item.toItemUiState(canSave: Boolean): ItemUiState = ItemUiState(
    id = id,
    date = date,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    canSave = canSave,
)