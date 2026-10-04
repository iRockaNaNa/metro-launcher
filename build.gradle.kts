// Top-level build file: plugin versions are declared here so all modules stay in sync.
// Kotlin 2.0+ ships the Compose compiler as a proper Gradle plugin
// (org.jetbrains.kotlin.plugin.compose).
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
