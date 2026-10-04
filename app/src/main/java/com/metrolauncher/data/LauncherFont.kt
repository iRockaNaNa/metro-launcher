package com.metrolauncher.data

/**
 * Font choices for the launcher-wide font picker (a modern twist on the
 * fixed Segoe UI of Windows 10 Mobile, inspired by MetroV's font options).
 */
enum class LauncherFont(val title: String, val description: String) {
    SEGOE("Segoe", "Windows 10 Mobile style (default)"),
    ROBOTO("Roboto", "Android system sans-serif"),
    MONOSPACE("Monospace", "Developer / terminal feel"),
    SERIF("Serif", "Classic editorial look"),
    ROUNDED("Rounded", "Soft, friendly curves"),
}
