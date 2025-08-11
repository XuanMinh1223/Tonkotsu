package com.nightfire.tonkotsu.core.data.di

import com.google.gson.Gson
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi // Your API interface
import com.nightfire.tonkotsu.core.data.repository.TonkotsuRepositoryImpl
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideJikanApi(okHttpClient: OkHttpClient, gson: Gson): JikanApi { // ADD gson parameter
        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Pass gson here
            .build()
            .create(JikanApi::class.java)
    }

    /**
     * Provides a singleton instance of [TonkotsuRepository].
     * Hilt will automatically inject the [JikanApi] instance.
     */
    @Provides
    @Singleton
    fun provideAnimeRepository(api: JikanApi): TonkotsuRepository {
        return TonkotsuRepositoryImpl(api)
    }

}