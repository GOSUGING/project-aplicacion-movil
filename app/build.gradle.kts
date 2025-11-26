plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")   // Compose Compiler para Kotlin 2.x
    id("com.google.devtools.ksp")              // KSP obligatorio
    id("com.google.dagger.hilt.android")       // Hilt
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

    // Jetpack Compose habilitado
    buildFeatures {
        compose = true
    }

    // ❌ IMPORTANTE: NO USAR composeOptions EN KOTLIN 2.1
    // composeOptions { }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(libs.gms.play.services.maps)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.material3)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.material)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Compose Tooling Debug
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Retrofit + Gson + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Room + KSP (OBLIGATORIO para que tu DbModule funcione)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    implementation("io.coil-kt:coil-compose:2.6.0")


    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
