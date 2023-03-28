package com.example.howmuchwasit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.howmuchwasit.ui.home.HomeScreen
import com.example.howmuchwasit.ui.item.AllItemListScreen
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
                navigateToAddItem = { navController.navigate(AddItem.route) },
                navigateToAllItemList = { navController.navigate(AllItemList.route) },
            )
        }

        // 아이템 추가 화면
        composable(route = AddItem.route) {
            ItemAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // 전체 아이템 리스트 화면
        composable(route = AllItemList.route) {
            AllItemListScreen(
                navigateToHome = { navController.popBackStack() },
                navigateToItemEdit = { navController.navigate(route = AddItem.route) },
            )
        }
    }
}