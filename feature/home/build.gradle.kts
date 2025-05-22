// feature/home/build.gradle.kts

plugins {
    alias(libs.plugins.android.library) // This is an Android Library module
    alias(libs.plugins.kotlin.android)  // For Kotlin in Android
    alias(libs.plugins.compose.compiler) // Compose compiler
    alias(libs.plugins.ksp)             // For Hilt's annotation processing (replaces kapt)
    alias(libs.plugins.hilt.android)    // For Hilt dependency injection
}

android {
    namespace = "com.nightfire.tonkotsu.feature.home" // IMPORTANT: Must match your package structure
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true // Enable Jetpack Compose in this module
    }
}

dependencies {
    // --- Android & Compose Core UI ---
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom)) // Use Compose BOM for consistent Compose versions
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // --- Multi-Module Dependency: Feature depends on Domain ---
    // This is crucial for accessing models, repositories interfaces, and use cases

    // --- Hilt Dependency Injection ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    // --- Other common libraries (e.g., Coil for image loading) ---
    implementation(libs.coil.compose)
}