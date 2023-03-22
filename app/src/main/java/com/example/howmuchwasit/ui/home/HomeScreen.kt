package com.example.howmuchwasit.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.HowMuchWasItTopAppBar
import com.example.howmuchwasit.ui.navigation.NavigationDestination.Home
import com.example.howmuchwasit.ui.theme.*

// 메인 화면 컴포저블
@Composable
fun HomeScreen(
    navigateToAddItem: () -> Unit,
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
            onCardClicked = navigateToAddItem,
            modifier = modifier.padding(innerPadding)
        )
    }
}

// 메인 화면 Body 컴포저블
@Composable
fun HomeBody(
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
                text = "추가",
                modifier = modifier.weight(1f),
                onCardClicked = onCardClicked
            )

            HomeItemCard(
                text = "최근 항목",
                modifier = modifier.weight(1f)
            )
        }

        HomeItemCard(
            text = "전체 목록",
            modifier = modifier.weight(1f)
        )

        Box(modifier = modifier.size(12.dp))
    }
}

// 메인화면 검색 TextField 컴보저블
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = {
            Text(
                text = stringResource(id = R.string.hint),
                color = Color.Gray,
                fontSize = 20.sp,
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
    text: String,
    onCardClicked: () -> Unit = {  },
    modifier: Modifier = Modifier,
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
                style = Typography.body1,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}