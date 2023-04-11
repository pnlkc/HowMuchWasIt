package com.example.howmuchwasit.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination.Home
import com.example.howmuchwasit.ui.theme.*

// 메인 화면 컴포저블
@Composable
fun HomeScreen(
    navigateToAddItem: () -> Unit,
    navigateToAllItemList: () -> Unit,
    navigateToRecentItemList: () -> Unit,
    navigateToItemList: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            HowMuchWasItTopAppBar(
                title = stringResource(id = Home.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        HomeBody(
            onAddItemCardClicked = navigateToAddItem,
            onAllItemListCardClicked = navigateToAllItemList,
            onRecentItemListCardClicked = navigateToRecentItemList,
            onSearchItemClicked = navigateToItemList,
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

// 메인 화면 Body 컴포저블
@Composable
fun HomeBody(
    onAddItemCardClicked: () -> Unit,
    onAllItemListCardClicked: () -> Unit,
    onRecentItemListCardClicked: () -> Unit,
    onSearchItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
) {
    val focusManager = LocalFocusManager.current

    val isSearchState = rememberSaveable { mutableStateOf(false) }
    val searchResult = viewModel.debounceSearchTerm.collectAsState()

    var backWait = 0L
    val context = LocalContext.current

    // 뒤로가기시 종료 안내 Toast 메세지
    BackHandler {
        if (isSearchState.value) {
            focusManager.clearFocus()
        } else {
            if (System.currentTimeMillis() - backWait >= 2000) {
                backWait = System.currentTimeMillis()
                Toast.makeText(context, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            } else {
                (context as? Activity)?.finish()
            }
        }
    }

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { if (!isSearchState.value) focusManager.clearFocus() }
            )
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HomeSearchBar(
            modifier = modifier
                .onFocusChanged { isSearchState.value = it.hasFocus },
            viewModel = viewModel
        )

        when (isSearchState.value) {
            true -> {
                SearchResultLazyColumn(
                    modifier = modifier
                        .weight(1f),
                    searchResult = searchResult.value.itemNameList,
                    onSearchItemClicked = onSearchItemClicked,
                )
            }
            false -> {
                Row(
                    modifier = modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeItemCard(
                        text = stringResource(id = R.string.add),
                        modifier = modifier.weight(1f),
                        onCardClicked = onAddItemCardClicked
                    )

                    HomeItemCard(
                        text = stringResource(id = R.string.recent_item),
                        modifier = modifier.weight(1f),
                        onCardClicked = onRecentItemListCardClicked
                    )
                }

                HomeItemCard(
                    text = stringResource(id = R.string.all_item_name_list),
                    modifier = modifier.weight(1f),
                    onCardClicked = onAllItemListCardClicked
                )
            }
        }

        Box(modifier = modifier.size(12.dp))
    }
}

// 메인화면 검색 TextField 컴보저블
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
) {
    // 텍스트 선택 손잡이(textSelectHandle), 텍스트 선택 배경(textColorHighlight) 변경
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MediumSlateBlue,
        backgroundColor = MediumSlateBlue.copy(alpha = 0.2f)
    )

    val text = viewModel.searchTerm.collectAsState()

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                viewModel.searchTerm.value = it
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.hint),
                    color = Color.Gray,
                    style = Typography.h3,
                )
            },
            trailingIcon = {
                if (text.value.isNotEmpty()) {
                    // 검색어 삭제 버튼
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = null,
                        tint = GraySuit,
                        modifier = Modifier
                            .clickable {
                                viewModel.searchTerm.value = ""
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = Black
                    )
                }
            },
            shape = Shapes.medium,
            textStyle = Typography.h3,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Portage,
                focusedBorderColor = MediumSlateBlue,
                textColor = Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

// 메인화면 아이템 카드 컴포저블 (여러 카드에서 재사용 가능하도록 구성)
@Composable
fun HomeItemCard(
    modifier: Modifier = Modifier,
    text: String,
    onCardClicked: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .clickable { onCardClicked() },
        shape = Shapes.medium
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = Typography.h1,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// 검색 결과 화면
@Composable
fun SearchResultLazyColumn(
    modifier: Modifier = Modifier,
    searchResult: List<String>,
    onSearchItemClicked: (String) -> Unit,
) {
    if (searchResult.isEmpty()) {
        ResultEmpty(
            painterResource = painterResource(id = R.drawable.search_no_result_icon),
            stringResource = stringResource(id = R.string.no_search_result)
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(searchResult) { item ->
                SearchResultItem(
                    item = item,
                    onSearchItemClicked = onSearchItemClicked
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    item: String,
    onSearchItemClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchItemClicked(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_inventory_24),
            contentDescription = null,
            tint = White,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GraySuit)
                .scale(0.6f)
        )

        Spacer(modifier = Modifier.size(12.dp))


        Text(
            text = item,
            style = Typography.h3,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.round_north_west_24),
            contentDescription = null,
            tint = GraySuit,
            modifier = Modifier.scale(1.0f)
        )
    }
}

@Composable
fun ResultEmpty(
    modifier: Modifier = Modifier,
    painterResource: Painter,
    stringResource: String,
    bottomSpaceOff: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource,
            contentDescription = null,
            tint = Black,
            modifier = Modifier
                .size(120.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))


        Text(
            text = stringResource,
            style = Typography.h3,
        )

        if (!bottomSpaceOff) Spacer(modifier = Modifier.size(50.dp))
    }
}