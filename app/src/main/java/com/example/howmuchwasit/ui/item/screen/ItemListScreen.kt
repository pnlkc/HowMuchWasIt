package com.example.howmuchwasit.ui.item.screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
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
import com.example.howmuchwasit.ui.home.ResultEmpty
import com.example.howmuchwasit.ui.item.ItemUiState
import com.example.howmuchwasit.ui.item.isValid
import com.example.howmuchwasit.ui.item.oneProductPrice
import com.example.howmuchwasit.ui.item.viewmodel.ItemListViewModel
import com.example.howmuchwasit.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ItemListScreen(
    modifier: Modifier = Modifier,
    navigateToAllItemNameList: () -> Unit,
    navigateToItemEdit: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemListViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = viewModel.name,
            canNavigateBack = canNavigateBack,
            navigateUp = navigateToAllItemNameList
        )
    }) { innerPadding ->
        ItemListBody(
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel,
            onItemClick = navigateToItemEdit,
        ) {
            coroutineScope.launch {
                viewModel.deleteItem(it)
            }
        }
    }
}

@Composable
fun ItemListBody(
    modifier: Modifier = Modifier,
    viewModel: ItemListViewModel,
    onItemClick: (Int) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    ItemListLazyColumn(
        modifier = modifier,
        viewModel = viewModel,
        onItemClick = { onItemClick(it.id) },
        onItemLongClick = onItemLongClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListLazyColumn(
    modifier: Modifier = Modifier,
    viewModel: ItemListViewModel,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    val itemList by viewModel.itemListUiState.collectAsState()
    val lowestItem by viewModel.itemUiState.collectAsState()

    if (itemList.itemList.isEmpty()) {
        ResultEmpty(
            painterResource = painterResource(id = R.drawable.no_item_list),
            stringResource = stringResource(id = R.string.no_item)
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (lowestItem.isValid()) {
                item {
                    LowestItem(
                        item = lowestItem
                    )
                }
            }

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
fun ItemListLazyColumnItem(
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

@Composable
fun DeleteDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        title = {
            Text(
                text = stringResource(id = R.string.caution),
                style = Typography.h3
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirm_delete),
                style = Typography.body1
            )
        },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(
                    text = stringResource(id = R.string.no),
                    color = MediumSlateBlue,
                    style = Typography.body1
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(
                    text = stringResource(id = R.string.yes),
                    color = MediumSlateBlue,
                    style = Typography.body1
                )
            }
        },
        backgroundColor = White,
        contentColor = contentColorFor(backgroundColor = White),
    )
}

@Composable
fun LowestItem(
    modifier: Modifier = Modifier,
    item: ItemUiState,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = 2.dp,
        border = BorderStroke(width = 2.dp, color = Pink)
    ) {
        Column(
            modifier = modifier
                .background(White)
                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.date,
                style = Typography.body1,
                color = Gray
            )

            Divider(color = Pink)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "최저가",
                    style = Typography.h3,
                    fontWeight = FontWeight.Bold,
                    color = LightRed
                )

                val priceSpannable = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = Typography.h3.fontSize * 0.7f)) {
                        append("개당 ")
                    }
                    append(item.oneProductPrice().toString())
                    withStyle(style = SpanStyle(fontSize = Typography.h3.fontSize * 0.7f)) {
                        append(" 원")
                    }
                }

                Text(
                    text = priceSpannable,
                    style = Typography.h3,
                    color = Black
                )
            }
        }
    }
}