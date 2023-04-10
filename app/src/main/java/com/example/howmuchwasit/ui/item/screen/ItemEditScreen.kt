package com.example.howmuchwasit.ui.item.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.item.viewmodel.ItemEditViewModel
import com.example.howmuchwasit.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

@Composable
fun ItemEditScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemEditViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        HowMuchWasItTopAppBar(
            title = stringResource(id = NavigationDestination.EditItem.titleRes),
            canNavigateBack = canNavigateBack,
            navigateUp = onNavigateUp
        )
    }) { innerPadding ->
        ItemAddBody(
            modifier = modifier.padding(innerPadding),
            navigateBack = navigateBack,
            itemUiState = viewModel.itemUiState,
            onItemValueChanged = viewModel::updateItemUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            datePick = viewModel.datePick
        )
    }
}