package com.example.howmuchwasit.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuchwasit.ui.AppViewModelProvider
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import com.example.howmuchwasit.ui.theme.*

@Composable
fun AllItemNameListScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToItemList: (String) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AllItemNameListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val itemListUiState by viewModel.itemNameListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.AllItemNameList.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = navigateToHome
        )
    }) { innerPadding ->
        AllItemNameListBody(
            modifier = modifier.padding(innerPadding),
            itemList = itemListUiState.itemNameList,
            onItemClick = navigateToItemList,
        )
    }
}

@Composable
fun AllItemNameListBody(
    modifier: Modifier = Modifier,
    itemList: List<String>,
    onItemClick: (String) -> Unit,
) {
    AllItemLazyList(
        itemList = itemList,
        onItemClick = { onItemClick(it) },
        modifier = modifier,
    )
}

@Composable
fun AllItemLazyList(
    modifier: Modifier = Modifier,
    itemList: List<String>,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = itemList, key = { it }) { item ->
            AllItemNameListLazyColumnItem(
                item = item,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
fun AllItemNameListLazyColumnItem(
    modifier: Modifier = Modifier,
    item: String,
    onItemClick: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onItemClick(item) },
        elevation = 2.dp
    ) {
        Text(
            text = item,
            style = Typography.h3,
            color = Black,
            modifier = modifier
                .background(White)
                .padding(24.dp)
        )
    }
}