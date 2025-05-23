package com.nightfire.tonkotsu // <--- IMPORTANT: Ensure this package matches your app's root package

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The custom Application class for Tonkotsu.
 * Annotated with @HiltAndroidApp to enable Hilt for the entire application.
 * This is the root component where Hilt's dependency graph starts.
 */
@HiltAndroidApp
class TonkotsuApplication : Application()