package com.matthias.dreamz.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.matthias.dreamz.R

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

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
fun DreamzTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        darkColors(
            primary = colorResource(id = android.R.color.system_accent1_500),
            primaryVariant = colorResource(id = android.R.color.system_accent1_800),
            secondary = colorResource(
                id = android.R.color.system_accent2_500
            ),
            secondaryVariant = colorResource(id = android.R.color.system_accent2_800)
        )
    } else {
        lightColors(
            primary = colorResource(id = android.R.color.system_accent1_500),
            primaryVariant = colorResource(id = android.R.color.system_accent1_800),
            secondary = colorResource(
                id = android.R.color.system_accent2_500
            ),
            secondaryVariant = colorResource(id = android.R.color.system_accent2_800)
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}