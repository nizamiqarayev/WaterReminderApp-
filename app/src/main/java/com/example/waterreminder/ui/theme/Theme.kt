package com.example.waterreminder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Cream = Color(0xFFFDF6EC)
val Peach = Color(0xFFFCE4D0)
val Sky = Color(0xFFB8DFF5)
val SkyDark = Color(0xFF7EC8E3)
val SkyDeep = Color(0xFF4BA8CC)
val Blush = Color(0xFFF9D5D5)
val Lavender = Color(0xFFE2D4F0)
val Green = Color(0xFFC8E6C3)
val TextColor = Color(0xFF4A3F35)
val TextLight = Color(0xFF9B8A7E)

private val LightColorScheme = lightColorScheme(
    primary = SkyDeep,
    secondary = Peach,
    tertiary = SkyDark,
    background = Cream,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = TextColor,
    onTertiary = Color.White,
    onBackground = TextColor,
    onSurface = TextColor,
)

@Composable
fun WaterReminderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}

