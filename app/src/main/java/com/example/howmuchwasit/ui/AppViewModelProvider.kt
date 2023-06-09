package com.example.howmuchwasit.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.howmuchwasit.HowMuchWasItApplication
import com.example.howmuchwasit.ui.home.HomeScreenViewModel
import com.example.howmuchwasit.ui.item.viewmodel.*

/**
 * Hilt 사용으로 인한 사용 중지
 * 대신 hiltViewModel()로 viewModel 인스턴스 제공
 */

//object AppViewModelProvider {
//    val Factory = viewModelFactory {
//        initializer {
//            ItemAddViewModel(
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//
//        initializer {
//            ItemListViewModel(
//                this.createSavedStateHandle(),
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//
//        initializer {
//            ItemEditViewModel(
//                this.createSavedStateHandle(),
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//
//        initializer {
//            RecentItemListViewModel(
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//
//        initializer {
//            HomeScreenViewModel(
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//
//        initializer {
//            AllItemNameListViewModel(
//                howMuchWasItApplication().container.itemRepository
//            )
//        }
//    }
//}
//
//fun CreationExtras.howMuchWasItApplication(): HowMuchWasItApplication =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HowMuchWasItApplication)