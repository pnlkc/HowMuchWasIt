package com.example.howmuchwasit.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination.Home
import com.example.howmuchwasit.ui.theme.*

// 메인 화면 컴포저블
@Composable
fun HomeScreen(
    navigateToAddItem: () -> Unit,
    navigateToAllItemList: () -> Unit,
    modifier: Modifier = Modifier,
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
            modifier = modifier.padding(innerPadding)
        )
    }
}

// 메인 화면 Body 컴포저블
@Composable
fun HomeBody(
    onAddItemCardClicked: () -> Unit,
    onAllItemListCardClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { focusManager.clearFocus() }
            )
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HomeSearchBar(modifier = modifier.weight(0.2f))

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
                onCardClicked = {  }
            )
        }

        HomeItemCard(
            text = stringResource(id = R.string.all_item_list),
            modifier = modifier.weight(1f),
            onCardClicked = onAllItemListCardClicked
        )

        Box(modifier = modifier.size(12.dp))
    }
}

// 메인화면 검색 TextField 컴보저블
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = {
            Text(
                text = stringResource(id = R.string.hint),
                color = Color.Gray,
                style = Typography.h3,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = Black
            )
        },
        shape = Shapes.medium,
        textStyle = Typography.h3,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Portage,
            focusedBorderColor = MediumSlateBlue,
            textColor = Black
        ),
        modifier = modifier
            .fillMaxSize()
    )
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
            .clickable { onCardClicked() }
        ,
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