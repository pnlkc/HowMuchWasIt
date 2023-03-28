package com.example.howmuchwasit.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.HowMuchWasItApplication
import com.example.howmuchwasit.data.AppContainer
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.data.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AllItemListViewModel(
    itemRepository: ItemRepository
) : ViewModel() {
    // private set으로 외부에서는 수정 불가능하게 설정
    val itemListUiState: StateFlow<ItemListUiState> = itemRepository.getAllItemsStream()
        .map { ItemListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemListUiState(val itemList: List<Item> = listOf())