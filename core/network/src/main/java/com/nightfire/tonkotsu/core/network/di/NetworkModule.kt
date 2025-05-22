package com.nightfire.tonkotsu.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.jikan.moe/v4/"

    /**
     * Provides a singleton instance of Gson for JSON serialization/deserialization.
     * `setLenient()` is used to be more forgiving with certain JSON parsing issues,
     * but it's generally better to have strict JSON if possible.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * Provides a singleton instance of HttpLoggingInterceptor.
     * This interceptor logs network requests and responses, which is invaluable for debugging.
     * The log level is set to BODY for comprehensive details.
     * For production builds, consider setting the level to NONE or HEADERS for security and performance.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides a singleton instance of OkHttpClient.
     * This client is configured with the logging interceptor and sets timeouts for network operations.
     * Hilt automatically injects `HttpLoggingInterceptor` because it's provided by another `@Provides` method.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor // Injected by Hilt
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // 30 seconds for connection establishment
            .readTimeout(30, TimeUnit.SECONDS)    // 30 seconds for reading data from the server
            .writeTimeout(30, TimeUnit.SECONDS)   // 30 seconds for writing data to the server
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit.
     * This is the core Retrofit builder, configured with the base URL, OkHttpClient,
     * and a GsonConverterFactory for automatic JSON parsing.
     * Hilt automatically injects `OkHttpClient` and `Gson` because they are provided by other `@Provides` methods.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, // Injected by Hilt
        gson: Gson // Injected by Hilt
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}