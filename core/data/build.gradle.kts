//data
// data/build.gradle.kts
plugins {
    alias(libs.plugins.android.library) // This module is an Android library
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)    // For Hilt's Android-specific features
}

android {
    namespace = "com.nightfire.tonkotsu.core.data"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // --- Module Dependencies ---
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    // --- Core Dependencies ---
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // --- Dependency Injection (Hilt) ---
    implementation(libs.hilt.android)
    implementation(libs.core.ktx)
    ksp(libs.hilt.android.compiler) // Annotation processor for Hilt

    // --- Network (Retrofit & OkHttp) ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // --- Local Persistence (Room) - Optional, but good to set up now if you plan to use it ---
    // Uncomment these lines if you plan to use Room
    // implementation(libs.room.runtime)
    // kapt(libs.room.compiler)
    // implementation(libs.room.ktx)

    // --- Testing (Standard for Android Libraries) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //paging
    implementation(libs.paging.runtime)
}