package com.stage.androidtv.data.repository

import com.stage.androidtv.data.local.MovieDao
import com.stage.androidtv.data.local.MovieEntity
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.remote.MovieApiService
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeMovieApiService(private val result: Result<List<MovieItem>>) : MovieApiService {
    override suspend fun getMovies(): List<MovieItem> {
        return result.getOrThrow()
    }
}

private class FakeMovieDao : MovieDao {
    private val storage = mutableListOf<MovieEntity>()

    override suspend fun getAllMovies(): List<MovieEntity> = storage.toList()

    override suspend fun insertAll(movies: List<MovieEntity>) {
        // Replace semantics by id
        val byId = storage.associateBy { it.id }.toMutableMap()
        for (m in movies) byId[m.id] = m
        storage.clear()
        storage.addAll(byId.values)
    }
}

class MovieRepositoryTest {
    @Test
    fun `returns remote data and caches to dao`() = runTest {
        // Given: a successful API and an empty DAO
        val remoteMovies = listOf(MovieItem("1", "A", "d", "2020", "g", "p", "v"))
        val api = FakeMovieApiService(Result.success(remoteMovies))
        val dao = FakeMovieDao()
        val repo = MovieRepository(api = api, movieDao = dao, ioDispatcher = StandardTestDispatcher(testScheduler))

        // When: fetching movies
        val result = repo.getMovies()

        // Then: returns remote data and caches it to DAO
        assertEquals(remoteMovies, result)
        assertEquals(1, dao.getAllMovies().size)
    }

    @Test
    fun `falls back to dao when api fails`() = runTest {
        // Given: a failing API and a DAO with cached data
        val dao = FakeMovieDao().apply {
            insertAll(listOf(MovieEntity("2", "B", "d2", "2021", "g2", "p2", "v2")))
        }
        val failingApi = object : MovieApiService {
            override suspend fun getMovies(): List<MovieItem> { throw RuntimeException("network error") }
        }
        val repo = MovieRepository(api = failingApi, movieDao = dao, ioDispatcher = StandardTestDispatcher(testScheduler))

        // When: fetching movies
        val result = repo.getMovies()

        // Then: returns cached data from DAO
        assertEquals(1, result.size)
        assertEquals("2", result.first().id)
    }
}


