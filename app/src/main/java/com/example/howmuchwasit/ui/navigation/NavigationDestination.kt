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


    // 전체 아이템 리스트 화면
    object AllItemList : NavigationDestination("all_item_list", R.string.all_item_list)

    // 아이템 세부정보 화면


    // 아이템 수정 화면


}