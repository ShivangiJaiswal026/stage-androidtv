package com.stage.androidtv.viewmodel

import com.stage.androidtv.data.local.MovieDao
import com.stage.androidtv.data.local.MovieEntity
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.remote.MovieApiService
import com.stage.androidtv.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeDaoVM : MovieDao {
    private val storage = mutableListOf<MovieEntity>()
    override suspend fun getAllMovies(): List<MovieEntity> = storage.toList()
    override suspend fun insertAll(movies: List<MovieEntity>) { storage.clear(); storage.addAll(movies) }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Success when repository returns movies`() = runTest {
        val movies = listOf(MovieItem("10", "Title", "Desc", "2022", "Genre", "Poster", "Video"))
        val repo = MovieRepository(
            api = object : MovieApiService { override suspend fun getMovies() = movies },
            movieDao = FakeDaoVM(),
            ioDispatcher = testDispatcher
        )

        val vm = MovieViewModel(repository = repo)

        advanceUntilIdle()

        val state = vm.state.value
        println("Current state: $state")

        assertTrue(
            "Expected Success but got $state",
            state is MovieState.Success && state.movies.isNotEmpty()
        )
    }

}
