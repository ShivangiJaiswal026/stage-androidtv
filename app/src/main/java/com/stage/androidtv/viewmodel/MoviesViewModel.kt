package com.stage.androidtv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stage.androidtv.core.ServiceLocator
import com.stage.androidtv.data.local.ApplicationDatabase
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.data.network.RetrofitClient
import com.stage.androidtv.data.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieState {
    object Loading : MovieState()
    data class Success(val movies: List<MovieItem>) : MovieState()
    data class Error(val message: String) : MovieState()
}

private const val NO_MOVIES_FOUND = "No movies found."

private const val SOMETHING_WENT_WRONG = "Something went wrong."

class MoviesViewModel(
    private val repository: MoviesRepository? = null
) : ViewModel() {

    private val repo by lazy {

        repository ?: MoviesRepository(
            RetrofitClient.api,
            ApplicationDatabase.getInstance(ServiceLocator.appContext).movieDao()
        )

    }

    private val _state = MutableStateFlow<MovieState>(MovieState.Loading)
    val state: StateFlow<MovieState> = _state

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                _state.value = MovieState.Loading
                val movies = repo.getMovies()

                if (movies.isNotEmpty()) {
                    _state.value = MovieState.Success(movies)
                } else {
                    _state.value = MovieState.Error(NO_MOVIES_FOUND)
                }

            } catch (e: Exception) {
                _state.value = MovieState.Error(e.message ?: SOMETHING_WENT_WRONG)
            }
        }
    }
}
