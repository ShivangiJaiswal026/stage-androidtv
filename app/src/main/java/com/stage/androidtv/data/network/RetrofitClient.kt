package com.stage.androidtv.data.network

import com.stage.androidtv.core.ServiceLocator
import com.stage.androidtv.data.remote.MovieApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/ShivangiJaiswal026/sample-movies-api/refs/heads/main/"

    private val okHttpClient: OkHttpClient by lazy {
        val context = ServiceLocator.appContext
        val cache = Cache(context.cacheDir, 5L * 1024L * 1024L) // 5MB
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=60") // 1 minute
                    .build()
            }
            .build()
    }

    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}
