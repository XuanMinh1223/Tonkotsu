// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nightfire.tonkotsu"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.nightfire.tonkotsu"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // --- Module Dependencies ---
    implementation(project(":feature:navigation"))
    implementation(project(":feature:home"))
    implementation(project(":feature:animedetail"))
    implementation(project(":feature:search"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":ui"))
    // App needs access to domain models and interfaces

    // --- Core Android & Compose Dependencies ---
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom)) // Crucial for managing Compose versions
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)

    // --- Lifecycle & ViewModel ---
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)

    // --- Hilt ---
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose) // Still used for hiltViewModel()
    ksp(libs.hilt.android.compiler)

    // --- Image Loading ---
    implementation(libs.coil.compose)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}