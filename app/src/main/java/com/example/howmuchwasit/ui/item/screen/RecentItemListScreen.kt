package com.example.howmuchwasit.ui.item.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.howmuchwasit.R
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.item.ItemListUiState
import com.example.howmuchwasit.ui.item.ItemUiState
import com.example.howmuchwasit.ui.item.isValid
import com.example.howmuchwasit.ui.item.oneProductPrice
import com.example.howmuchwasit.ui.item.viewmodel.RecentItemListViewModel
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RecentItemListScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToItemEdit: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RecentItemListViewModel = hiltViewModel()
) {
    val itemList by viewModel.itemListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.RecentItemList.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = navigateToHome
        )
    }) { innerPadding ->
        RecentItemListBody(
            modifier = modifier.padding(innerPadding),
            itemList = itemList,
            onItemClick = navigateToItemEdit,
        ) {
            coroutineScope.launch {
                viewModel.deleteItem(it)
            }
        }
    }
}

@Composable
fun RecentItemListBody(
    modifier: Modifier = Modifier,
    itemList: ItemListUiState,
    onItemClick: (Int) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    RecentItemListLazyColumn(
        modifier = modifier,
        itemList = itemList,
        onItemClick = { onItemClick(it.id) },
        onItemLongClick = onItemLongClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentItemListLazyColumn(
    modifier: Modifier = Modifier,
    itemList: ItemListUiState,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (itemList.itemList.isEmpty()) {
            item {
                Text("기록이 존재하지 않습니다")
            }
        } else {
            items(items = itemList.itemList, key = { it.id }) { item ->
                ItemListLazyColumnItem(
                    modifier = modifier.animateItemPlacement(),
                    item = item,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentItemListLazyColumnItem(
    modifier: Modifier = Modifier,
    item: Item,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    // 항목을 삭제하는 경우 다이얼로그 보여주기 위한 변수
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = { onItemClick(item) },
                onLongClick = { deleteConfirmationRequired = true }
            ),
        elevation = 2.dp
    ) {
        Column(
            modifier = modifier
                .background(White)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.date,
                style = Typography.body1,
                color = Gray
            )

            Divider(color = Portage)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = if (item.quantity == 1) 12.dp else 0.dp)
            ) {
                // AnnotatedString 생성 (Spannable String과 유사한 기능이지만 Compose 전용)
                val nameSpannable = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(item.name)
                    }

                    withStyle(
                        style = SpanStyle(
                            color = Gray,
                            fontSize = Typography.h3.fontSize * 0.7f
                        )
                    ) {
                        append(if (item.quantity != 1) " (${item.quantity}개)" else "")
                    }
                }

                Text(
                    text = nameSpannable,
                    style = Typography.h3,
                    color = Black,
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    val priceSpannable = buildAnnotatedString {
                        append(item.price.toString())
                        withStyle(
                            style = SpanStyle(
                                fontSize = Typography.h3.fontSize * 0.7f
                            )
                        ) {
                            append(" 원")
                        }
                    }

                    Text(
                        text = priceSpannable,
                        style = Typography.h3,
                        color = Black
                    )

                    if (item.price != item.onePrice) {
                        Spacer(modifier = modifier.size(8.dp))

                        Text(
                            text = "개당  " + item.onePrice.toString() + " 원",
                            style = Typography.body1,
                            fontStyle = FontStyle.Italic,
                            color = Gray
                        )
                    }
                }
            }
        }
    }

    if (deleteConfirmationRequired) {
        DeleteDialog(
            onDeleteCancel = { deleteConfirmationRequired = false },
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onItemLongClick(item)
            }
        )
    }
}