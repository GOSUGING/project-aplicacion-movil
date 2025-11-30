plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("jacoco")
}

android {
    namespace = "com.example.levelup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.levelup"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Jetpack Compose
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // Evita errores por archivos META-INF duplicados en dependencias (LICENSE.md, etc.)
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
    }
}

dependencies {
    // ------------------------------------------------
    // Use the Compose BOM from version catalog (libs.versions.toml)
    // ------------------------------------------------
    implementation(platform(libs.androidx.compose.bom))

    // AndroidX core & lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose (managed by BOM)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Optional compose artifacts present in toml
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.ui.geometry)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit / OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coil
    implementation(libs.coil.compose)

    // Room + KSP
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Play Services (maps, location) — from version catalog
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Debug tooling for Compose
    debugImplementation(libs.androidx.ui.tooling)

    // ------------------------------------------------
    // TESTING - UNIT TESTS (src/test)
    // ------------------------------------------------
    // Unify JUnit 5 using BOM so transitives don't bring old 5.8.x artifacts
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Kotest (runner + assertions) — keep the versions you used
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // MockK for unit tests
    testImplementation("io.mockk:mockk:1.13.10")

    // ------------------------------------------------
    // INSTRUMENTED / ANDROID TESTS (androidTest)
    // ------------------------------------------------
    androidTestImplementation(libs.androidx.junit)          // androidx.test.ext:junit
    androidTestImplementation(libs.androidx.espresso.core) // espresso-core

    // Apply Compose BOM also for androidTest so ui-test-junit4 resolves without explicit version
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // MockK for instrumented tests
    androidTestImplementation("io.mockk:mockk-android:1.13.10")
    androidTestImplementation("io.mockk:mockk-agent:1.13.10")
}

// Use JUnit Platform for unit tests (JUnit 5)
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// JaCoCo configuration (left as you had it before if you want to keep)
jacoco {
    toolVersion = "0.8.10"
}
