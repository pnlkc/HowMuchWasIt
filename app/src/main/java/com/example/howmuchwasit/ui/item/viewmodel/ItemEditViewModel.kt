package com.example.howmuchwasit.ui.item.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.data.toItemUiState
import com.example.howmuchwasit.ui.home.HomeScreenViewModel
import com.example.howmuchwasit.ui.item.ItemNameListUiState
import com.example.howmuchwasit.ui.item.ItemUiState
import com.example.howmuchwasit.ui.item.isValid
import com.example.howmuchwasit.ui.item.toItem
import com.example.howmuchwasit.ui.navigation.NavigationDestination.EditItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[EditItem.itemIdArg])

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    val itemNameListUiState: StateFlow<ItemNameListUiState> = itemRepository.getAllItemsNameStream()
        .map { ItemNameListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemNameListUiState()
        )

    // 날짜 선택 다이얼로그 결과 저장용 변수
    val datePick = mutableStateOf(LocalDate.now())

    val searchTerm = MutableStateFlow("")

    // debounce로 검색 기능 구현
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val debounceSearchTerm = searchTerm
        .debounce(50)
        .distinctUntilChanged()
        .flatMapLatest { searchTermValue ->
            itemRepository.getSearchItemStream(searchTermValue)
                .map { listItem ->
                    ItemNameListUiState(listItem)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemNameListUiState()
        )

    init {
        loadItemUiStateAndSetDateInfo()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // itemUiState 값 업데이트
    fun updateItemUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(canSave = newItemUiState.isValid())
    }

    // 선택된 아이템의 ItemUiState 값 로드 및 datePick.value 설정
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