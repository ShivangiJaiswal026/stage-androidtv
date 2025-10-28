package com.stage.androidtv.data.network

import com.stage.androidtv.data.model.MovieItem
import retrofit2.http.GET

interface MockApiService {
    @GET("movies.json")
    suspend fun getMovies(): List<MovieItem>
}