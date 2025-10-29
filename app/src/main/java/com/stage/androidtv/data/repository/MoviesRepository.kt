package com.stage.androidtv.data.repository

import android.util.Log
import com.stage.androidtv.data.local.MoviesDao
import com.stage.androidtv.data.local.MovieEntity
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.remote.MoviesApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val GET_MOVIES = "GET_MOVIES"
private const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

class MoviesRepository(
    private val api: MoviesApiService,
    private val moviesDao: MoviesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMovies(): List<MovieItem> = withContext(ioDispatcher) {
        try {
            val remote = api.getMovies()
            val entities = remote.map { it.toEntity() }
            moviesDao.insertAll(entities)
            remote
        } catch (e: Exception) {
            Log.e(GET_MOVIES, e.message ?: UNKNOWN_ERROR)
            val cached = moviesDao.getAllMovies()
            if (cached.isNotEmpty()) cached.map { it.toDomain() } else emptyList()
        }
    }
}

private fun MovieItem.toEntity(): MovieEntity =
    MovieEntity(
        id = id,
        title = title,
        description = description,
        year = year,
        genre = genre,
        posterUrl = posterUrl,
        videoUrl = videoUrl
    )

private fun MovieEntity.toDomain(): MovieItem =
    MovieItem(
        id = id,
        title = title,
        description = description,
        year = year,
        genre = genre,
        posterUrl = posterUrl,
        videoUrl = videoUrl
    )
