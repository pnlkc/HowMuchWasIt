package com.example.howmuchwasit.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.data.Item
import com.example.howmuchwasit.ui.AppViewModelProvider
import com.example.howmuchwasit.ui.theme.*

@Composable
fun AllItemListScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToItemEdit: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AllItemListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val itemListUiState by viewModel.itemListUiState.collectAsState()

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
            onItemClick = navigateToItemEdit
        )
    }
}

@Composable
fun AllItemListBody(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    onItemClick: () -> Unit,
) {
    AllItemLazyList(
        itemList = itemList,
        onItemClick = onItemClick
    )
}

@Composable
fun AllItemLazyList(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    onItemClick: () -> Unit,
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
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun LazyListItem(
    modifier: Modifier = Modifier,
    item: Item,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onItemClick() },
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
                    append(item.name)
                    withStyle(style = SpanStyle(
                        color = Gray,
                        fontSize = Typography.h3.fontSize * 0.7f)
                    ) {
                        append(if (item.quantity != 1) " (${item.quantity}개)" else "")
                    }
                }

                Text(
//                    text = item.name + if (item.quantity != 1) " (${item.quantity}개)" else "",
                    text = nameSpannable,
                    style = Typography.h3,
                    color = Black
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    val priceSpannable = buildAnnotatedString {
                        append(item.price.toString())
                        withStyle(style = SpanStyle(
                            fontSize = Typography.h3.fontSize * 0.7f)
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
}