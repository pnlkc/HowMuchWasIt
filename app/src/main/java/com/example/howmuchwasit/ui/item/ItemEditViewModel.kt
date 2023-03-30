package com.example.howmuchwasit.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.data.toItemUiState
import com.example.howmuchwasit.ui.navigation.NavigationDestination.EditItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[EditItem.itemIdArg])

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    // 날짜 선택 다이얼로그 결과 저장용 변수
    val datePick = mutableStateOf(LocalDate.now())

    init {
        loadItemUiStateAndSetDateInfo()
    }

    // itemUiState 값 업데이트
    fun updateItemUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(canSave = newItemUiState.isValid())
    }


    private fun loadItemUiStateAndSetDateInfo() {
        viewModelScope.launch {
            itemUiState = itemRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(canSave = true)

            datePick.value = LocalDate.parse(
                itemUiState.date,
                DateTimeFormatter.ofPattern("yyyy / M / d")
            )
        }
    }

    // 아이템 저장 기능
    suspend fun updateItem() {
        if (itemUiState.isValid()) itemRepository.updateItem(itemUiState.toItem())
    }
}