package com.example.howmuchwasit.ui.navigation

import com.example.howmuchwasit.R

// 구글 코드랩의 예시 코드는 interface - object 패턴을 사용하여 NavigationDestination을 구현했지만,
// 이 앱은 sealed class - object 패턴을 사용하여 한 클래스 파일에서 전체 route를 관리합니다.
sealed class NavigationDestination(val route: String, val titleRes: Int) {
    // 홈화면
    object Home : NavigationDestination("home", R.string.app_name)

    // 아이템 추가 화면
    object AddItem : NavigationDestination("add_item", R.string.add_item)

    // 최근 아이템 리스트 화면
    object RecentItemList : NavigationDestination("recent_item_list", R.string.recent_item)

    // 전체 아이템 이름 리스트 화면
    object AllItemNameList : NavigationDestination("all_item_list", R.string.all_item_name_list)

    // 특정 아이템 리스트 화면
    object ItemList : NavigationDestination("item_list", R.string.item_list) {
        const val nameArg = "name"
    }

    // 아이템 수정 화면
    object EditItem : NavigationDestination("edit_item", R.string.edit_item) {
        const val itemIdArg = "itemId"
    }
}