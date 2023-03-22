package com.example.howmuchwasit.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.howmuchwasit.R
import com.example.howmuchwasit.ui.navigation.HowMuchWasItNavHost

// 앱의 최상위 컴포저블
@Composable
fun HowMuchWasItApp(navController: NavHostController = rememberNavController()) {
    HowMuchWasItNavHost(navController)
}

// 상단바 컴포저블
@Composable
fun HowMuchWasItTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {  }
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            elevation = 0.dp
        )
    } else {
        TopAppBar(
            title = { Text(title) },
            modifier = modifier,
            elevation = 0.dp
        )
    }
}