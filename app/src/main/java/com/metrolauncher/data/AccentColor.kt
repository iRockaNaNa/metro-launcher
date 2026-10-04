package com.metrolauncher.data

import androidx.compose.ui.graphics.Color

/**
 * The 20 authentic Windows Phone 8.1 accent colors. Used by the accent
 * color picker and as the default tile coloring.
 */
enum class AccentColor(val title: String, val color: Color) {
    LIME("lime", Color(0xFFA4C400)),
    GREEN("green", Color(0xFF60A917)),
    EMERALD("emerald", Color(0xFF008A00)),
    TEAL("teal", Color(0xFF00ABA9)),
    CYAN("cyan", Color(0xFF1BA1E2)),
    COBALT("cobalt", Color(0xFF0050EF)),
    INDIGO("indigo", Color(0xFF6A00FF)),
    VIOLET("violet", Color(0xFFAA00FF)),
    PINK("pink", Color(0xFFF472D0)),
    MAGENTA("magenta", Color(0xFFD80073)),
    CRIMSON("crimson", Color(0xFFA20025)),
    RED("red", Color(0xFFE51400)),
    ORANGE("orange", Color(0xFFFA6800)),
    AMBER("amber", Color(0xFFF0A30A)),
    YELLOW("yellow", Color(0xFFE3C800)),
    BROWN("brown", Color(0xFF825A2C)),
    OLIVE("olive", Color(0xFF6D8764)),
    STEEL("steel", Color(0xFF647687)),
    MAUVE("mauve", Color(0xFF76608A)),
    TAUPE("taupe", Color(0xFF87794E)),
}
