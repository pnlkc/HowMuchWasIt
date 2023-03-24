package com.example.howmuchwasit.ui.item

// 아이템 상태 데이터 클래스
data class ItemUiState(
    val id: Int = 0,
    val date: String = "",
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val isLocked: Boolean = false
)


// 아이템 상태를 Room Entity 클래스로 변경
fun ItemUiState.toItem() {

}

// 아이템 상태가 올바른지 확인
fun ItemUiState.isValid() : Boolean {
    return name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
}
