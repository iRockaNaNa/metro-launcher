package com.metrolauncher.ui.applist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.metrolauncher.data.AccentColor

/**
 * The Windows Phone "jumplist": a full-screen letter grid overlay.
 * Letters that have apps are shown in the accent color; the rest are dimmed.
 */
@Composable
fun JumpListOverlay(
    availableLetters: Set<Char>,
    accent: AccentColor,
    onLetterClick: (Char) -> Unit,
    onDismiss: () -> Unit,
) {
    // A-Z plus "#" for the non-letter bucket, matching the list grouping.
    val letters = ('A'..'Z').toList() + '#'

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f)),
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(letters) { letter ->
                    val available = availableLetters.contains(letter)
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable(enabled = available) { onLetterClick(letter) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = letter.toString(),
                            fontSize = 28.sp,
                            color = if (available) accent.color
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                        )
                    }
                }
            }
        }
    }
}
