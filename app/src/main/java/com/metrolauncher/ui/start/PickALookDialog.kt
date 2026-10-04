package com.metrolauncher.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metrolauncher.data.AccentColor

/**
 * First-run "pick a look" step, inspired by MetroV's onboarding that sets
 * everything up at once: theme + accent in a single dialog.
 */
@Composable
fun PickALookDialog(
    onFinish: (darkTheme: Boolean, accent: AccentColor) -> Unit,
) {
    var darkTheme by remember { mutableStateOf(true) }
    var accent by remember { mutableStateOf(AccentColor.COBALT) }

    AlertDialog(
        onDismissRequest = { /* must pick a look to continue */ },
        title = { Text("pick a look") },
        text = {
            Column {
                Text(
                    text = "Choose a starting theme. You can change everything later in settings.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("dark theme", modifier = Modifier.weight(1f))
                    Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                }
                AccentColor.entries.take(10).chunked(5).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        row.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(40.dp)
                                    .background(option.color, CircleShape)
                                    .clickable { accent = option },
                            )
                        }
                    }
                }
                Text(
                    text = "showing 10 of ${AccentColor.entries.size} accents — the rest are in settings.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onFinish(darkTheme, accent) }) {
                Text("let's go")
            }
        },
    )
}
