// core/domain/build.gradle.kts

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    // Add the android block, similar to your other core modules
    namespace = "com.nightfire.tonkotsu.core.domain" // Ensure this matches your package
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        // minSdk is required for Android libraries, even if not used directly
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Standard Kotlin library for any Kotlin module
    implementation(libs.kotlin.stdlib)
    // Kotlin Coroutines for async operations (which your repositories will use)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // IMPORTANT: Add the dependency to core:common here!
    implementation(project(":core:common")) // <--- ADD THIS LINE (if you haven't yet)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
}