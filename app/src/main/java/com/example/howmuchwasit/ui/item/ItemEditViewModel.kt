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

class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[EditItem.itemIdArg])

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    init {
        loadItemUiState()
    }

    // itemUiState 값 업데이트
    fun updateItemUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(canSave = newItemUiState.isValid())
    }


    private fun loadItemUiState() {
        viewModelScope.launch {
            itemUiState = itemRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(canSave = true)
        }
    }

    // 아이템 저장 기능
    suspend fun updateItem() {
        if (itemUiState.isValid()) itemRepository.updateItem(itemUiState.toItem())
    }
}