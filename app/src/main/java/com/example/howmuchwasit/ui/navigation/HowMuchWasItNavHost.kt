package com.example.howmuchwasit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.howmuchwasit.ui.home.HomeScreen
import com.example.howmuchwasit.ui.item.ItemAddScreen
import com.example.howmuchwasit.ui.navigation.NavigationDestination.*

// 앱의 NavHost를 따로 분리하여 관리
@Composable
fun HowMuchWasItNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        // 메인 화면
        composable(route = Home.route) {
            HomeScreen(
                navigateToAddItem = { navController.navigate(AddItem.route) }
            )
        }

        composable(route = AddItem.route) {
            ItemAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}