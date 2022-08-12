package com.beyondidentity.authenticator.sdk.android.composeui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BiPrimaryMain,
    primaryVariant = BiPrimaryMain,
    secondary = BiPrimaryMain,
    // surface = BiGray200Dark,
    // background = BiGray200Dark,
)

private val LightColorPalette = lightColors(
    primary = BiPrimaryMain,
    primaryVariant = BiPrimaryMain,
    secondary = BiPrimaryMain,
    // background = Color.White,
    // surface = Color.White,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BiSdkAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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
