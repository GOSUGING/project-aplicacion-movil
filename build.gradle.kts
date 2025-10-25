// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Plugin de Android para módulos de aplicación
    alias(libs.plugins.android.application) apply false

    // Plugin de Kotlin para Android
    alias(libs.plugins.kotlin.android) apply false

    // Plugin del Compilador de Jetpack Compose
    // Esto resuelve la advertencia "Starting in Kotlin 2.0..."
    alias(libs.plugins.compose.compiler) apply false
}
