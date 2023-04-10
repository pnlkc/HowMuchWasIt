package com.example.howmuchwasit.ui.item.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.item.ItemNameListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AllItemNameListViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    val itemNameListUiState: StateFlow<ItemNameListUiState> = itemRepository.getAllItemsNameStream()
        .map { ItemNameListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemNameListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item = item)
    }
}