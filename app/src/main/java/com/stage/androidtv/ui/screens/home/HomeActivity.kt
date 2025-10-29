package com.stage.androidtv.ui.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stage.androidtv.core.ServiceLocator
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.ui.common.Watermark
import com.stage.androidtv.ui.screens.player.VideoPlayerActivity
import com.stage.androidtv.ui.theme.AndroidTVTheme
import com.stage.androidtv.viewmodel.MovieState
import com.stage.androidtv.viewmodel.MoviesViewModel

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            AndroidTVTheme {
                Surface(Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: MoviesViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when (state) {
            is MovieState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is MovieState.Error -> Text(
                text = (state as MovieState.Error).message,
                modifier = Modifier.padding(24.dp)
            )

            is MovieState.Success -> {
                val movies = (state as MovieState.Success).movies
                MovieListScreen(movies)
            }
        }

        Watermark(alignment = Alignment.TopEnd)
    }
}

@Composable
fun MovieListScreen(movies: List<MovieItem>) {
    val context = LocalContext.current
    var selectedMovie by remember { mutableStateOf(movies.firstOrNull()) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(Modifier.fillMaxSize()) {
        selectedMovie?.let {
            Image(
                painter = rememberAsyncImagePainter(it.posterUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.25f }
            )
        }

        Row(Modifier.fillMaxSize()) {
            SideNavigationRail(selectedIndex) { selectedIndex = it }

            Column(
                Modifier
                    .weight(1f)
                    .padding(24.dp)
            ) {
                selectedMovie?.let { movie ->
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        modifier = Modifier.padding(start = 28.dp, end = 154.dp)
                    )
                    Text(
                        text = movie.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(
                            start = 28.dp,
                            end = 254.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Text(
                        text = "${movie.genre} | ${movie.year}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 28.dp, end = 254.dp, top = 8.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(movies.size) { index ->
                        val movie = movies[index]
                        MovieTile(
                            movie = movie,
                            onFocus = {
                                if (selectedMovie?.id != movie.id) {
                                    selectedMovie = movie
                                }
                            },
                            onPlay = {
                                val intent = Intent(context, VideoPlayerActivity::class.java)
                                intent.putExtra("videoUrl", movie.videoUrl)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SideNavigationRail(selected: Int, onSelect: (Int) -> Unit) {
    NavigationRail(
        containerColor = Color(0xFF1C1C1C)
    ) {
        NavigationRailItem(
            selected = selected == 0,
            onClick = { onSelect(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) }
        )
        NavigationRailItem(
            selected = selected == 1,
            onClick = { onSelect(1) },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White) }
        )
        NavigationRailItem(
            selected = selected == 2,
            onClick = { onSelect(2) },
            icon = {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = Color.White
                )
            }
        )
        NavigationRailItem(
            selected = selected == 3,
            onClick = { onSelect(3) },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        )
    }
}

@Composable
fun MovieTile(
    movie: MovieItem,
    onFocus: () -> Unit,
    onPlay: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = tween(150, easing = LinearOutSlowInEasing)
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp, 250.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onFocusChanged {
                isFocused = it.isFocused
                if (isFocused) onFocus()
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER &&
                    keyEvent.nativeKeyEvent.action == android.view.KeyEvent.ACTION_UP
                ) {
                    onPlay()
                    true
                } else false
            }
            .focusable(),
        border = if (isFocused) BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)) else null,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(if (isFocused) 10.dp else 2.dp)
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
