// build.gradle.kts (a nivel de proyecto)

plugins {
    // Plugin de Android para módulos de aplicación
    alias(libs.plugins.android.application) apply false

    // Plugin de Kotlin para Android
    alias(libs.plugins.kotlin.android) apply false

    // NOTA: Hemos eliminado 'kotlin.compose' y 'ksp' porque no son necesarios
    // con la configuración estable que hemos definido, y estaban causando el error.
}
