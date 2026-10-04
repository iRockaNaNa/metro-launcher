@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.metrolauncher.ui.start

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metrolauncher.data.AccentColor
import com.metrolauncher.data.IconStyle
import com.metrolauncher.data.PinnedTile
import com.metrolauncher.data.TileSize
import com.metrolauncher.util.toImageBitmap
import kotlinx.coroutines.delay

/**
 * A single Start-screen tile.
 *
 * Medium and wide tiles perform the signature Windows Phone "live tile"
 * flip every few seconds (MetroV does the same with its dynamic tiles);
 * small tiles stay static, like the original.
 */
@Composable
fun MetroTile(
    tile: PinnedTile,
    tileColor: Color,
    iconStyle: IconStyle,
    loadIcon: (PinnedTile) -> android.graphics.drawable.Drawable?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Live-tile flip state; only medium+ tiles flip.
    var flipped by remember(tile.packageName, tile.activityName) { mutableStateOf(false) }
    if (tile.size != TileSize.SMALL) {
        LaunchedEffect(tile.packageName, tile.activityName) {
            while (true) {
                delay(4500)
                flipped = !flipped
            }
        }
    }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "tileFlip",
    )
    val density = LocalDensity.current.density

    val shape = when (iconStyle) {
        IconStyle.MODERN_ROUNDED -> RoundedCornerShape(12.dp)
        else -> RoundedCornerShape(0.dp) // sharp Metro corners
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(tileColor)
            .graphicsLayer {
                rotationY = rotation
                // Push the virtual camera back so the 3D flip doesn't distort.
                cameraDistance = 12f * density
            }
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        if (rotation <= 90f) {
            TileFront(
                tile = tile,
                iconStyle = iconStyle,
                loadIcon = loadIcon,
            )
        } else {
            // Back face: mirror the rotation so the content reads correctly.
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tile.label,
                    color = Color.White,
                    fontSize = if (tile.size == TileSize.WIDE) 22.sp else 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
    }
}

@Composable
private fun TileFront(
    tile: PinnedTile,
    iconStyle: IconStyle,
    loadIcon: (PinnedTile) -> android.graphics.drawable.Drawable?,
) {
    val drawable = remember(tile.packageName, tile.activityName) { loadIcon(tile) }
    val imageBitmap = remember(drawable) { drawable?.toImageBitmap() }

    Box(Modifier.fillMaxSize()) {
        if (imageBitmap != null) {
            when (iconStyle) {
                IconStyle.METRO_GLYPH -> {
                    // Classic Windows Phone look: white pictogram centered.
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = tile.label,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize(0.55f)
                            .align(Alignment.Center),
                    )
                }
                IconStyle.MODERN_ROUNDED -> {
                    // Original icon, softly rounded container.
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.72f)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White.copy(alpha = 0.92f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = tile.label,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                IconStyle.MONO_OUTLINE -> {
                    // Minimal monochrome treatment.
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = tile.label,
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.85f)),
                        contentScale = ContentScale.Fit,
                        alpha = 0.85f,
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .align(Alignment.Center),
                    )
                }
            }
        } else {
            // Fallback when the icon can't be resolved: first letter, Metro style.
            Text(
                text = tile.label.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // App name, bottom-left — the Windows Phone signature.
        Text(
            text = tile.label,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 6.dp, end = 8.dp),
        )
    }
}

/** Resolves the effective tile color: per-tile override or the global accent. */
fun PinnedTile.resolveColor(globalAccent: AccentColor): Color =
    accentName?.let { runCatching { AccentColor.valueOf(it) }.getOrNull() }?.color
        ?: globalAccent.color
