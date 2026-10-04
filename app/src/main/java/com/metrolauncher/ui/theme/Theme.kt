package com.metrolauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.metrolauncher.data.LauncherFont

/**
 * App theme: Windows Phone defaulted to a black background with a single
 * accent color, so dark theme is the default here too. The [accent] and
 * [font] come straight from the user's picks in Settings.
 */
@Composable
fun MetroLauncherTheme(
    darkTheme: Boolean = true,
    accent: Color,
    font: LauncherFont,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = accent,
            secondary = accent,
            tertiary = accent,
            surface = Color.Black,
            background = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
        )
    } else {
        lightColorScheme(
            primary = accent,
            secondary = accent,
            tertiary = accent,
            surface = Color.White,
            background = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black,
        )
    }

    val typography = Typography().withFontFamily(font.toFontFamily())

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}
