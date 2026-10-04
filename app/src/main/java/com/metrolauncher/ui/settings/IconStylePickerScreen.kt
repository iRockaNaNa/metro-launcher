package com.metrolauncher.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.metrolauncher.data.AccentColor
import com.metrolauncher.data.IconStyle

/**
 * The icon-style picker — the second headline modern twist, inspired by
 * MetroV's icon-pack support and per-icon tweaks. Each option shows a
 * live preview tile in the current accent color.
 */
@Composable
fun IconStylePickerScreen(
    selected: IconStyle,
    accent: AccentColor,
    onSelect: (IconStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = "icon style",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = "How app icons appear on your tiles.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp),
        )

        IconStyle.entries.forEach { style ->
            IconStyleOptionRow(
                style = style,
                accent = accent,
                selected = style == selected,
                onClick = { onSelect(style) },
            )
        }
    }
}

@Composable
private fun IconStyleOptionRow(
    style: IconStyle,
    accent: AccentColor,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        RadioButton(selected = selected, onClick = onClick)

        // Mini preview tile demonstrating the style.
        val shape = when (style) {
            IconStyle.MODERN_ROUNDED -> RoundedCornerShape(10.dp)
            else -> RoundedCornerShape(0.dp)
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(56.dp)
                .clip(shape)
                .background(accent.color),
            contentAlignment = Alignment.Center,
        ) {
            when (style) {
                IconStyle.METRO_GLYPH -> Text("◉", color = Color.White)
                IconStyle.MODERN_ROUNDED -> Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center,
                ) { Text("◉", color = accent.color) }
                IconStyle.MONO_OUTLINE -> Text(
                    "◯",
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = style.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = style.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
    }
}
