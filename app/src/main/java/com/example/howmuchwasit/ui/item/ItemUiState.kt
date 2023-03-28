package com.example.howmuchwasit.ui.item

import androidx.core.text.isDigitsOnly
import com.example.howmuchwasit.data.Item

// 아이템 상태 데이터 클래스
data class ItemUiState(
    val id: Int = 0,
    val date: String = "",
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val canSave: Boolean = false
)


// ItemUiState를 Room Entity 클래스로 변경
fun ItemUiState.toItem() = Item(
    id = id,
    date = date,
    name = name,
    price = price.toInt(),
    quantity = quantity.toInt(),
    onePrice = onProductPrice(),
)

fun ItemUiState.onProductPrice() = price.toInt() / quantity.toInt()


fun ItemUiState.isPriceDigitsOnly() = price.isDigitsOnly()
fun ItemUiState.isQuantityDigitsOnly() = quantity.isDigitsOnly()

// 아이템 상태가 올바른지 확인
fun ItemUiState.isValid(): Boolean {
    val nameCheck = name.isNotBlank()
    val priceCheck = price.isNotBlank() && isPriceDigitsOnly()
    val quantityCheck = quantity.isNotBlank() && isQuantityDigitsOnly() && quantity.toInt() > 0
    return nameCheck && priceCheck && quantityCheck
}
