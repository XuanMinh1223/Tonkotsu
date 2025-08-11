package com.nightfire.tonkotsu.core.network

import com.google.gson.Gson
import com.nightfire.tonkotsu.core.network.di.NetworkModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest {

    private lateinit var networkModule: NetworkModule

    @Before
    fun setup() {
        networkModule = NetworkModule
    }

    @Test
    fun `provideGson provides a non-null Gson instance`() {
        val gson = networkModule.provideGson()
        assertNotNull(gson)
    }

    @Test
    fun `provideHttpLoggingInterceptor provides interceptor with BODY level`() {
        val interceptor = networkModule.provideHttpLoggingInterceptor()
        assertNotNull(interceptor)
        assertEquals(HttpLoggingInterceptor.Level.BODY, interceptor.level)
    }

    @Test
    fun `provideOkHttpClient provides client with logging interceptor and correct timeouts`() {
        // We need to provide a mock interceptor for this test
        val mockLoggingInterceptor = HttpLoggingInterceptor()
        val client = networkModule.provideOkHttpClient(mockLoggingInterceptor)

        assertNotNull(client)
        assertEquals(30000, client.connectTimeoutMillis)
        assertEquals(30000, client.readTimeoutMillis)
        assertEquals(30000, client.writeTimeoutMillis)

        // Verify the logging interceptor is added
        val hasLoggingInterceptor = client.interceptors.any { it is HttpLoggingInterceptor }
        assertTrue(hasLoggingInterceptor)
    }

    @Test
    fun `provideRetrofit provides Retrofit with correct base URL and converters`() {
        // We need to provide mock OkHttpClient and Gson for this test
        val mockOkHttpClient = OkHttpClient.Builder().build()
        val mockGson = Gson()

        val retrofit = networkModule.provideRetrofit(mockOkHttpClient, mockGson)

        assertNotNull(retrofit)
        assertEquals("https://api.jikan.moe/v4/", retrofit.baseUrl().toString())
        assertEquals(mockOkHttpClient, retrofit.callFactory()) // Retrofit uses OkHttpClient as Call.Factory

        // Verify GsonConverterFactory is added
        val hasGsonConverter = retrofit.converterFactories().any { it is GsonConverterFactory }
        assertTrue(hasGsonConverter)
    }
}