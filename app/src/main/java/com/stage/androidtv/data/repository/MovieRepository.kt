package com.stage.androidtv.data.repository

import com.stage.androidtv.data.local.MovieDao
import com.stage.androidtv.data.local.MovieEntity
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.remote.MovieApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MovieApiService,
    private val movieDao: MovieDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMovies(): List<MovieItem> = withContext(ioDispatcher) {
        try {
            val remote = api.getMovies()
            val entities = remote.map { it.toEntity() }
            movieDao.insertAll(entities)
            remote
        } catch (e: Exception) {
            val cached = movieDao.getAllMovies()
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
