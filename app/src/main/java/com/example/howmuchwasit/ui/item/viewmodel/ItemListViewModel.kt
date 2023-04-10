package com.example.howmuchwasit.ui.item.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.data.toItemUiState
import com.example.howmuchwasit.ui.item.ItemListUiState
import com.example.howmuchwasit.ui.item.ItemUiState
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemRepository
) : ViewModel() {
    val name: String = checkNotNull(savedStateHandle[NavigationDestination.ItemList.nameArg])

    val itemListUiState: StateFlow<ItemListUiState> = itemRepository.getItemsListStream(name)
        .map { ItemListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemListUiState()
        )

    val itemUiState: StateFlow<ItemUiState> = itemRepository.getLowestItemStream(name)
        .map { it?.toItemUiState(false) ?: ItemUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item = item)
    }
}