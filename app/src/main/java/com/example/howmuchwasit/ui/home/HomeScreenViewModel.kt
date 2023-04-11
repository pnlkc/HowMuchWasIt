package com.example.howmuchwasit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.item.ItemNameListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    val searchTerm = MutableStateFlow("")

    // debounce로 검색 기능 구현
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val debounceSearchTerm = searchTerm
        .debounce(350)
        .distinctUntilChanged()
        .flatMapLatest { searchTermValue ->
            itemRepository.getSearchItemStream(searchTermValue)
                .map { listItem ->
                    if (searchTermValue.isBlank()) ItemNameListUiState()
                    else ItemNameListUiState(listItem)
                }
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