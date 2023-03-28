package com.example.howmuchwasit.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.howmuchwasit.data.ItemRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemAddViewModel(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    // private set으로 외부에서는 수정 불가능하게 설정
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    init {
        initItemUiState()
    }

    // itemUiState 값 업데이트
    fun updateItemUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(canSave = newItemUiState.isValid())
    }

    // ItemUiState 초기값 설정
    private fun initItemUiState() {
        val currentDate = DateTimeFormatter.ofPattern("yyyy / M / d").format(LocalDate.now())
        itemUiState = itemUiState.copy(
            date = currentDate,
            quantity = "1"
        )
    }

    // 아이템 저장 기능
    suspend fun saveItem() {
        if (itemUiState.isValid()) itemRepository.addItem(itemUiState.toItem())
    }
}