package com.example.howmuchwasit.ui.item.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howmuchwasit.data.ItemRepository
import com.example.howmuchwasit.ui.item.ItemNameListUiState
import com.example.howmuchwasit.ui.item.ItemUiState
import com.example.howmuchwasit.ui.item.isValid
import com.example.howmuchwasit.ui.item.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ItemAddViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {
    // private set으로 외부에서는 수정 불가능하게 설정
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    // 아이템 이름 리스트
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
        initItemUiState()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    // itemUiState 값 업데이트
    fun updateItemUiState(newItemUiState: ItemUiState) {
        itemUiState = newItemUiState.copy(canSave = newItemUiState.isValid())
    }

    // ItemUiState 초기값 설정
    private fun initItemUiState() {
        val currentDate = DateTimeFormatter.ofPattern("yyyy / M / d").format(LocalDate.now())
        itemUiState = itemUiState.copy(
            date = currentDate,
            quantity = "1"
        )
    }

    // 아이템 저장 기능
    suspend fun saveItem() {
        if (itemUiState.isValid()) itemRepository.addItem(itemUiState.toItem())
    }
}