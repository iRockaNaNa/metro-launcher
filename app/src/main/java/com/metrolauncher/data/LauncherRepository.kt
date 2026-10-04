package com.metrolauncher.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.provider.MediaStore

/**
 * An installed, launchable app shown in the app list.
 *
 * Icons are loaded lazily via [loadIcon] because resolving every drawable
 * up front is expensive on devices with hundreds of apps.
 */
data class AppEntry(
    val label: String,
    val packageName: String,
    val activityName: String,
) {
    val componentName: ComponentName
        get() = ComponentName(packageName, activityName)
}

/**
 * Talks to PackageManager: lists launchable apps, launches them, resolves
 * icons, and seeds a Windows-Phone-like default Start layout on first run.
 */
class LauncherRepository(private val context: Context) {

    private val packageManager: PackageManager
        get() = context.packageManager

    /** All launchable apps, sorted alphabetically (case-insensitive). */
    fun getInstalledApps(): List<AppEntry> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(intent, 0)
            .map { info ->
                AppEntry(
                    label = info.loadLabel(packageManager).toString(),
                    packageName = info.activityInfo.packageName,
                    activityName = info.activityInfo.name,
                )
            }
            .sortedBy { it.label.lowercase() }
    }

    fun loadIcon(entry: AppEntry): Drawable? =
        runCatching {
            packageManager.getActivityIcon(entry.componentName)
        }.getOrNull()

    /** Launches the app behind [entry]; returns false if it could not start. */
    fun launchApp(entry: AppEntry): Boolean = launch(entry.componentName)

    /** Launches the app behind [tile]; returns false if it could not start. */
    fun launchTile(tile: PinnedTile): Boolean =
        launch(ComponentName(tile.packageName, tile.activityName))

    private fun launch(component: ComponentName): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(component.packageName)
            ?: Intent(Intent.ACTION_MAIN)
                .setComponent(component)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching {
            context.startActivity(intent)
            true
        }.getOrNull() ?: false
    }

    /**
     * Builds a familiar out-of-box Start layout (Phone, Messaging, Camera,
     * Browser, …) from whatever of those is actually installed. Only used
     * when the user has never pinned anything.
     */
    fun seedDefaultTiles(): List<PinnedTile> {
        val candidates = listOf(
            // Dialer
            Triple(Intent(Intent.ACTION_DIAL), TileSize.MEDIUM, "Phone"),
            // SMS
            Triple(Intent(Intent.ACTION_MAIN).apply { type = "vnd.android-dir/mms-sms" }, TileSize.MEDIUM, "Messaging"),
            // Camera
            Triple(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TileSize.MEDIUM, "Camera"),
            // Browser
            Triple(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://example.com")), TileSize.WIDE, "Browser"),
            // Settings
            Triple(Intent(android.provider.Settings.ACTION_SETTINGS), TileSize.SMALL, "Settings"),
            // Contacts
            Triple(Intent(Intent.ACTION_VIEW).apply { type = "vnd.android.cursor.dir/contact" }, TileSize.SMALL, "Contacts"),
        )
        return candidates.mapNotNull { (intent, size, fallbackLabel) ->
            val resolved = packageManager.resolveActivity(intent, 0)?.activityInfo ?: return@mapNotNull null
            PinnedTile(
                packageName = resolved.packageName,
                activityName = resolved.name,
                label = resolved.loadLabel(packageManager)?.toString() ?: fallbackLabel,
                size = size,
            )
        }.distinctBy { it.packageName }.take(8)
    }
}
