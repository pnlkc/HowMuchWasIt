package com.example.howmuchwasit.ui.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.item.ItemListUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RecentItemListViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {
    // private set으로 외부에서는 수정 불가능하게 설정
    val itemListUiState: StateFlow<ItemListUiState> = itemRepository.getRecentItemStream()
        .map { ItemListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item = item)
    }
}