package com.example.howmuchwasit.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.howmuchwasit.HowMuchWasItApplication
import com.example.howmuchwasit.ui.item.AllItemListViewModel
import com.example.howmuchwasit.ui.item.ItemAddViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ItemAddViewModel(
                howMuchWasItApplication().container.itemRepository
            )
        }

        initializer {
            AllItemListViewModel(
                howMuchWasItApplication().container.itemRepository
            )
        }
    }
}

fun CreationExtras.howMuchWasItApplication(): HowMuchWasItApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HowMuchWasItApplication)