plugins {
    alias(libs.plugins.android.library) // Applies the Android Library plugin
    alias(libs.plugins.kotlin.android)  // Applies the Kotlin Android plugin
}

android {
    namespace = "com.nightfire.tonkotsu.core.common" // IMPORTANT: Must match your package structure
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Or whatever JVM target you are using
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" // Or whatever JVM target you are using
    }
}

dependencies {
    // Basic Android KTX extensions (good for utility modules)
    implementation(libs.core.ktx)
    // The Kotlin Standard Library (explicitly declared for clarity)
    implementation(libs.kotlin.stdlib)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}