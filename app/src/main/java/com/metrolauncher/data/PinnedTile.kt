package com.metrolauncher.data

/**
 * A tile pinned to the Start screen.
 *
 * @param accentName optional [AccentColor] name overriding the global accent
 * for this tile; null means "use the global accent".
 */
data class PinnedTile(
    val packageName: String,
    val activityName: String,
    val label: String,
    val size: TileSize,
    val accentName: String? = null,
)

/**
 * Minimal pipe-delimited serialization so we can persist tiles in DataStore
 * without adding a serialization library. Format per tile:
 * `package|activity|label|SIZE|accentName` (accent may be empty).
 * Tiles are joined with `;;`. Labels are sanitized on write.
 */
private const val FIELD_SEPARATOR = "|"
private const val TILE_SEPARATOR = ";;"

private fun sanitize(value: String): String =
    value.replace(FIELD_SEPARATOR, " ").replace(TILE_SEPARATOR, " ")

fun PinnedTile.serialize(): String = listOf(
    packageName,
    activityName,
    sanitize(label),
    size.name,
    accentName ?: ""
).joinToString(FIELD_SEPARATOR)

fun deserializeTile(raw: String): PinnedTile? {
    val parts = raw.split(FIELD_SEPARATOR)
    if (parts.size < 4) return null
    val size = runCatching { TileSize.valueOf(parts[3]) }.getOrNull() ?: return null
    return PinnedTile(
        packageName = parts[0],
        activityName = parts[1],
        label = parts[2],
        size = size,
        accentName = parts.getOrNull(4)?.takeIf { it.isNotBlank() },
    )
}

fun List<PinnedTile>.serializeAll(): String =
    joinToString(TILE_SEPARATOR) { it.serialize() }

fun deserializeTiles(raw: String?): List<PinnedTile> {
    if (raw.isNullOrBlank()) return emptyList()
    return raw.split(TILE_SEPARATOR).mapNotNull { deserializeTile(it) }
}
