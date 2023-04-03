package com.example.howmuchwasit.ui.item

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ItemListViewModel(
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item = item)
    }
}