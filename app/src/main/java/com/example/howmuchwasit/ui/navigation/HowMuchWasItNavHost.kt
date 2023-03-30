package com.example.howmuchwasit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.howmuchwasit.ui.home.HomeScreen
import com.example.howmuchwasit.ui.item.AllItemListScreen
import com.example.howmuchwasit.ui.item.ItemAddScreen
import com.example.howmuchwasit.ui.item.ItemEditScreen
import com.example.howmuchwasit.ui.item.RecentItemListScreen
import com.example.howmuchwasit.ui.navigation.NavigationDestination.*

// 앱의 NavHost를 따로 분리하여 관리
@Composable
fun HowMuchWasItNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
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
                navigateToRecentItemList = { navController.navigate(RecentItemList.route) },
            )
        }

        // 아이템 추가 화면
        composable(route = AddItem.route) {
            ItemAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToAllItemList = {
                    navController.navigate(
                        route = AllItemList.route,
                        navOptions = NavOptions.Builder().setPopUpTo(Home.route, false).build()
                    )
                }
            )
        }

        // 전체 아이템 리스트 화면
        composable(route = AllItemList.route) {
            AllItemListScreen(
                navigateToHome = { navController.popBackStack() },
                navigateToItemEdit = { navController.navigate(route = "${EditItem.route}/${it}") },
            )
        }

        // 최근 아이템 리스트 화면
        composable(route = RecentItemList.route) {
            RecentItemListScreen(
                navigateToHome = { navController.popBackStack() },
                navigateToItemEdit = { navController.navigate(route = "${EditItem.route}/${it}") }
            )
        }

        // 아이템 수정 화면
        composable(
            // {${EditItem.itemIdArg}} 는 매개변수 자리표시자로
            // {itemId}는 argument로 들어오는 값과 매치됩니다.
            // 때문에 위의 "${EditItem.route}/${it}"과 "${EditItem.route}/{${EditItem.itemIdArg}}"는 일치하게 됩니다.
            route = "${EditItem.route}/{${EditItem.itemIdArg}}",
            arguments = listOf(navArgument(EditItem.itemIdArg) { type = NavType.IntType })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}