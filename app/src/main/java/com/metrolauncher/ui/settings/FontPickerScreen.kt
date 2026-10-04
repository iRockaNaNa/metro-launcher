package com.metrolauncher.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metrolauncher.data.LauncherFont
import com.metrolauncher.ui.theme.toFontFamily

/**
 * The font picker — one of the two headline modern twists. Each option
 * previews in its own typeface so the user can see the effect immediately.
 */
@Composable
fun FontPickerScreen(
    selected: LauncherFont,
    onSelect: (LauncherFont) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = "choose your font",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = "Applies everywhere in the launcher — tiles, app list, settings.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp),
        )

        LauncherFont.entries.forEach { font ->
            FontOptionRow(
                font = font,
                selected = font == selected,
                onClick = { onSelect(font) },
            )
        }
    }
}

@Composable
private fun FontOptionRow(
    font: LauncherFont,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val family: FontFamily = font.toFontFamily()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = font.title,
                fontFamily = family,
                fontSize = 22.sp,
            )
            Text(
                text = "AaBbCc 123 — ${font.description}",
                fontFamily = family,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    }
}
