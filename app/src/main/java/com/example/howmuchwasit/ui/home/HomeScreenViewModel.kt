package com.example.howmuchwasit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.item.ItemListUiState
import com.example.howmuchwasit.ui.item.ItemNameListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class HomeScreenViewModel(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    val searchTerm = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val debounceSearchTerm = searchTerm
        .debounce(350)
        .distinctUntilChanged()
        .flatMapLatest {
            itemRepository.getSearchItemStream(searchTerm.value)
                .map { listItem -> ItemNameListUiState(listItem) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemNameListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}