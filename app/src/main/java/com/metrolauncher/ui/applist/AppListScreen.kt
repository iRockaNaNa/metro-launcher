@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.metrolauncher.ui.applist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metrolauncher.data.AccentColor
import com.metrolauncher.data.AppEntry
import com.metrolauncher.data.TileSize
import kotlinx.coroutines.launch

/**
 * The all-apps list, Windows 10 Mobile style: alphabetical sections with
 * sticky letter headers, a search box, and a "jumplist" letter-grid overlay
 * for fast scrolling.
 *
 * Long-press an app to pin it to Start (with a tile-size choice).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppListScreen(
    apps: List<AppEntry>,
    accent: AccentColor,
    onAppClick: (AppEntry) -> Unit,
    onPinApp: (AppEntry, TileSize) -> Unit,
    isPinned: (AppEntry) -> Boolean,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var showJumpList by remember { mutableStateOf(false) }
    var pinCandidate by remember { mutableStateOf<AppEntry?>(null) }

    val filtered = remember(apps, query) {
        if (query.isBlank()) apps
        else apps.filter { it.label.contains(query, ignoreCase = true) }
    }
    // Group by first letter; non-letters go under "#", like the original.
    val sections = remember(filtered) {
        filtered.groupBy { entry ->
            entry.label.firstOrNull()?.uppercaseChar()?.takeIf { it.isLetter() } ?: '#'
        }.toSortedMap()
    }
    // Flat list positions so the jumplist can scroll to a section.
    val sectionStartIndexes = remember(sections) {
        var index = 1 // position 1: the search box is item 0
        sections.mapValues { (_, entries) ->
            val start = index
            index += 1 + entries.size // header + rows
            start
        }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("search apps") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )
            }
            sections.forEach { (letter, entries) ->
                stickyHeader(key = "header-$letter") {
                    LetterHeader(letter = letter, accent = accent)
                }
                items(
                    items = entries,
                    key = { it.packageName + "/" + it.activityName },
                ) { app ->
                    AppRow(
                        app = app,
                        accent = accent,
                        pinned = isPinned(app),
                        onClick = { onAppClick(app) },
                        onLongClick = { pinCandidate = app },
                    )
                }
            }
        }

        // Floating jumplist button, bottom-end — opens the letter grid.
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(accent.color)
                .clickable { showJumpList = true },
            contentAlignment = Alignment.Center,
        ) {
            Text("#", color = Color.White, fontSize = 20.sp)
        }

        if (showJumpList) {
            JumpListOverlay(
                availableLetters = sections.keys,
                accent = accent,
                onLetterClick = { letter ->
                    showJumpList = false
                    sectionStartIndexes[letter]?.let { index ->
                        scope.launch { listState.animateScrollToItem(index) }
                    }
                },
                onDismiss = { showJumpList = false },
            )
        }

        pinCandidate?.let { app ->
            PinToStartDialog(
                app = app,
                alreadyPinned = isPinned(app),
                onPin = { size ->
                    onPinApp(app, size)
                    pinCandidate = null
                },
                onDismiss = { pinCandidate = null },
            )
        }
    }
}

/** Pin dialog with a tile-size choice. */
@Composable
private fun PinToStartDialog(
    app: AppEntry,
    alreadyPinned: Boolean,
    onPin: (TileSize) -> Unit,
    onDismiss: () -> Unit,
) {
    var size by remember { mutableStateOf(TileSize.MEDIUM) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(app.label) },
        text = {
            if (alreadyPinned) {
                Text("This app is already pinned to Start.")
            } else {
                Column {
                    Text("Choose a tile size, then pin to Start.")
                    TileSize.entries.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { size = option },
                        ) {
                            RadioButton(
                                selected = size == option,
                                onClick = { size = option },
                            )
                            Text(option.title)
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!alreadyPinned) {
                TextButton(onClick = { onPin(size) }) { Text("pin to start") }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("cancel") }
        },
    )
}

@Composable
private fun LetterHeader(letter: Char, accent: AccentColor) {
    Text(
        text = letter.toString(),
        color = accent.color,
        fontSize = 22.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
    )
}

@Composable
private fun AppRow(
    app: AppEntry,
    accent: AccentColor,
    pinned: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Small accent square with the initial — the WP app-list row look.
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(accent.color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = app.label.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 18.sp,
            )
        }
        Text(
            text = app.label,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (pinned) {
            Text(
                text = "pinned",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        }
    }
}
