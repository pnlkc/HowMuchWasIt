package com.example.howmuchwasit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.howmuchwasit.ui.HowMuchWasItApp
import com.example.howmuchwasit.ui.theme.HowMuchWasItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        var screenHeightDp: Float = 0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = resources.displayMetrics
        screenHeightDp = displayMetrics.heightPixels / displayMetrics.density

        Log.d("로그", "screenHeightDp = $screenHeightDp")


        setContent {
            HowMuchWasItTheme {
                HowMuchWasItApp()
            }
        }
    }
}