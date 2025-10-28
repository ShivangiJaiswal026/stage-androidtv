package com.stage.androidtv.ui.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.ui.screens.player.VideoPlayerActivity
import com.stage.androidtv.ui.theme.AndroidtvTheme
import com.stage.androidtv.viewmodel.MovieState
import com.stage.androidtv.viewmodel.MovieViewModel

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidtvTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: MovieViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is MovieState.Loading -> Text("Loading...", modifier.padding(16.dp))
        is MovieState.Error -> Text((state as MovieState.Error).message, modifier.padding(16.dp))
        is MovieState.Success -> MovieListScreen((state as MovieState.Success).movies)
    }
}

@Composable
fun MovieListScreen(movies: List<MovieItem>) {
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(movies) { movie ->
                MovieTile(movie) {
                    val intent = Intent(context, VideoPlayerActivity::class.java)
                    intent.putExtra("videoUrl", movie.videoUrl)
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MovieTile(movie: MovieItem, onClick: () -> Unit) {
    var focused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(120.dp)
            .height(380.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable(true),
        colors = CardDefaults.cardColors(
            containerColor = if (focused)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.posterUrl),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
