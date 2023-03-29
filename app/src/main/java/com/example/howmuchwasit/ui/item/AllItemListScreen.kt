package com.example.howmuchwasit.ui.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.howmuchwasit.R
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.ui.AppViewModelProvider
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AllItemListScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToItemEdit: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AllItemListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val itemListUiState by viewModel.itemListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.AllItemList.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = navigateToHome
        )
    }) { innerPadding ->
        AllItemListBody(
            modifier = modifier.padding(innerPadding),
            itemList = itemListUiState.itemList,
            onItemClick = navigateToItemEdit,
            onItemLongClick = {
                coroutineScope.launch {
                    viewModel.deleteItem(it)
                }
            }
        )
    }
}

@Composable
fun AllItemListBody(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    AllItemLazyList(
        itemList = itemList,
        onItemClick = { onItemClick(it.id) },
        modifier = modifier,
        onItemLongClick = onItemLongClick
    )
}

@Composable
fun AllItemLazyList(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(items = itemList, key = { it.id }) { item ->
            LazyListItem(
                item = item,
                onItemClick = onItemClick,
                onItemLongClick = onItemLongClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyListItem(
    modifier: Modifier = Modifier,
    item: Item,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
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
        contentColor = contentColorFor(backgroundColor = White)
    )
}