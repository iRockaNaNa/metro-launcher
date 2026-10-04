package com.metrolauncher.data

/**
 * Icon styles for tiles — the second modern twist, inspired by MetroV's
 * icon-pack support and per-icon tweaks.
 */
enum class IconStyle(val title: String, val description: String) {
    METRO_GLYPH(
        "Metro glyphs",
        "Classic white pictograms on the accent tile (Windows Phone look)"
    ),
    MODERN_ROUNDED(
        "Modern rounded",
        "Original app icons on soft rounded tiles"
    ),
    MONO_OUTLINE(
        "Mono outline",
        "Minimal monochrome icon treatment"
    ),
}
