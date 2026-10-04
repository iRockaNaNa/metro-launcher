package com.metrolauncher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import com.metrolauncher.data.LauncherFont

/**
 * Maps the user's font pick to a Compose [FontFamily].
 *
 * Built-in families are used as stand-ins so the picker works out of the
 * box; bundling real Segoe UI / rounded font files under
 * `res/font/` and referencing them here is the natural next step
 * (see the README roadmap).
 */
fun LauncherFont.toFontFamily(): FontFamily = when (this) {
    LauncherFont.SEGOE -> FontFamily.Default
    LauncherFont.ROBOTO -> FontFamily.SansSerif
    LauncherFont.MONOSPACE -> FontFamily.Monospace
    LauncherFont.SERIF -> FontFamily.Serif
    // Closest built-in to a rounded geometric sans; replace with a bundled
    // font (e.g. Quicksand/Nunito) for the real rounded look.
    LauncherFont.ROUNDED -> FontFamily.Cursive
}

/** Applies [fontFamily] to every Material3 text style. */
fun Typography.withFontFamily(fontFamily: FontFamily): Typography = copy(
    displayLarge = displayLarge.copy(fontFamily = fontFamily),
    displayMedium = displayMedium.copy(fontFamily = fontFamily),
    displaySmall = displaySmall.copy(fontFamily = fontFamily),
    headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
    titleLarge = titleLarge.copy(fontFamily = fontFamily),
    titleMedium = titleMedium.copy(fontFamily = fontFamily),
    titleSmall = titleSmall.copy(fontFamily = fontFamily),
    bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = bodySmall.copy(fontFamily = fontFamily),
    labelLarge = labelLarge.copy(fontFamily = fontFamily),
    labelMedium = labelMedium.copy(fontFamily = fontFamily),
    labelSmall = labelSmall.copy(fontFamily = fontFamily),
)
