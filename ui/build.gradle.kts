// Tonkotsu/ui/build.gradle.kts

plugins {
    alias(libs.plugins.android.library) // This module is an Android library
    alias(libs.plugins.kotlin.android)    // For Kotlin Android features
    alias(libs.plugins.ksp)               // For Kotlin Symbol Processing (e.g., if you add Hilt later or other annotation processors)
}

android {
    namespace = "com.nightfire.tonkotsu.ui" // Ensure this matches your package name
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Only needed if you have Android tests in this module
    }

    // Enable Compose for this module
    buildFeatures {
        compose = true
    }

    // Specify the Compose Compiler Extension version
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    // --- AndroidX Core ---
    implementation(libs.core.ktx)
    implementation(libs.kotlin.stdlib)

    // --- Compose Dependencies ---
    // Import the Compose BOM to manage versions
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3) // If you use Material Design 3 components
    debugImplementation(libs.ui.tooling) // For Compose tooling in debug builds

    // --- Lifecycle & ViewModel (if your common UI components need them, e.g., for rememberSaveable) ---
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)

    //Coil
    implementation(libs.coil.compose)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}