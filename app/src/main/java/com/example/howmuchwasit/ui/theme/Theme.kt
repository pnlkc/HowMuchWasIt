package com.example.howmuchwasit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = White,
    primaryVariant = Purple700,
    onPrimary = Black,
    secondary = Teal200,
    surface = Portage,
    onSurface = White
)

private val LightColorPalette = lightColors(
    primary = White,
    primaryVariant = Purple700,
    onPrimary = Black,
    secondary = Teal200,
    surface = Portage,
    onSurface = White
)

@Composable
fun HowMuchWasItTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}