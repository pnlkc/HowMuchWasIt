package com.example.howmuchwasit.ui.item.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.howmuchwasit.ui.AppViewModelProvider
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.item.viewmodel.RecentItemListViewModel
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

@Composable
fun RecentItemListScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToItemEdit: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RecentItemListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val itemListUiState by viewModel.itemListUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.RecentItemList.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = navigateToHome
        )
    }) { innerPadding ->
        ItemListBody(
            modifier = modifier.padding(innerPadding),
            lowestItem = null,
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