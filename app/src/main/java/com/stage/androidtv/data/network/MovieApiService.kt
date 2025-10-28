package com.stage.androidtv.data.network

import com.stage.androidtv.data.model.MovieItem
import retrofit2.http.GET

interface MovieApiService {
    @GET("samplemovies.json")
    suspend fun getMovies(): List<MovieItem>
}