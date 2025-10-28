package com.stage.androidtv.data.repository

import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.network.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val apiService: MovieApiService
) {
    suspend fun getMovies(): List<MovieItem> = withContext(Dispatchers.IO) {
        try {
            apiService.getMovies()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}