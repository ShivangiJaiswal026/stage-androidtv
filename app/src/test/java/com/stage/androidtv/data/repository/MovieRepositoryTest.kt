package com.stage.androidtv.data.repository

import com.stage.androidtv.data.local.MoviesDao
import com.stage.androidtv.data.local.MovieEntity
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.remote.MoviesApiService
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeMoviesApiService(private val result: Result<List<MovieItem>>) : MoviesApiService {
    override suspend fun getMovies(): List<MovieItem> {
        return result.getOrThrow()
    }
}

private class FakeMoviesDao : MoviesDao {
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
    fun `fetch movies success response returns remote data and caches to dao`() = runTest {
        //given
        val remoteMovies = listOf(MovieItem("1", "A", "d", "2020", "g", "p", "v"))
        val api = FakeMoviesApiService(Result.success(remoteMovies))
        val dao = FakeMoviesDao()
        val repo = MoviesRepository(
            api = api,
            moviesDao = dao,
            ioDispatcher = StandardTestDispatcher(testScheduler)
        )
        //when
        val result = repo.getMovies()

        //then
        assertEquals(remoteMovies, result)
        assertEquals(1, dao.getAllMovies().size)
    }

    @Test
    fun `fetch movies error response falls back to dao when api fails`() = runTest {
        //given
        val dao = FakeMoviesDao().apply {
            insertAll(listOf(MovieEntity("2", "B", "d2", "2021", "g2", "p2", "v2")))
        }
        val failingApi = object : MoviesApiService {
            override suspend fun getMovies(): List<MovieItem> {
                throw RuntimeException("network  error")
            }
        }
        val repo = MoviesRepository(
            api = failingApi,
            moviesDao = dao,
            ioDispatcher = StandardTestDispatcher(testScheduler)
        )

        //when
        val result = repo.getMovies()

        //then
        assertEquals(1, result.size)
        assertEquals("2", result.first().id)
    }
}


