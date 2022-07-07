package com.beyondidentity.authenticator.sdk.android.composeui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BiPrimaryMain = Color(0xFF4673D3)
val BiSecondary = Color(0xFF2484FF)

val BiGray300Light = Color(0xFFEEEEEE)

///// DARK ////////////
val BiGray200Dark = Color(0xFF1c1c1c)
val BiGray300Dark = Color(0xFF333333)

// @get:Composable
// val Colors.myExtraColor: Color
//     get() = if (isSystemInDarkTheme()) Color.Red else Color.Green

val Colors.BiGray300: Color
    @Composable
    get() = if (isSystemInDarkTheme()) BiGray300Dark else BiGray300Light

val Colors.BiAppBarColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) MaterialTheme.colors.background else Color.White
