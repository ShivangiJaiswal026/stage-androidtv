package com.stage.androidtv.data.network

import com.stage.androidtv.core.ServiceLocator
import com.stage.androidtv.data.remote.MoviesApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //url to get sample data from my other github repo
    private const val BASE_URL =
        "https://raw.githubusercontent.com/ShivangiJaiswal026/sample-movies-api/refs/heads/main/"

    private val okHttpClient: OkHttpClient by lazy {
        val context = ServiceLocator.appContext
        val cache = Cache(context.cacheDir, 5L * 1024L * 1024L)
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=60")
                    .build()
            }
            .build()
    }

    val api: MoviesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApiService::class.java)
    }
}
