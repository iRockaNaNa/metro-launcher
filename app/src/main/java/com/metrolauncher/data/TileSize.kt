package com.metrolauncher.data

/**
 * Tile sizes, mirroring Windows 10 Mobile: small (1x1), medium (2x2) and
 * wide (4x2) tiles on a 4-column Start grid.
 */
enum class TileSize(val columns: Int, val rows: Int) {
    SMALL(1, 1),
    MEDIUM(2, 2),
    WIDE(4, 2);

    val title: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }
}
