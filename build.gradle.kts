plugins {
    id("com.android.application") version "8.13.1" apply false
    id("com.android.library") version "8.13.1" apply false

    id("org.jetbrains.kotlin.android") version "2.1.0" apply false

    // 🔥 KSP compatible
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false

    // 🔥 Hilt
    id("com.google.dagger.hilt.android") version "2.51" apply false

    // ⭐ OBLIGATORIO PARA KOTLIN 2.0+
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
}
