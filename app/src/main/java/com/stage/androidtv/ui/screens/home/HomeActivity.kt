package com.stage.androidtv.ui.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.stage.androidtv.data.model.MovieItem
import com.stage.androidtv.ui.screens.player.VideoPlayerActivity
import com.stage.androidtv.ui.theme.AndroidtvTheme

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
private fun HomeScreen(modifier: Modifier = Modifier) {
    Text(text = "Home", modifier = modifier)
}

@Composable
fun MovieListScreen() {
    val context = LocalContext.current
    val movies = remember {
        listOf(
            MovieItem(
                "1",
                "Interstellar",
                "A space odyssey",
                "https://picsum.photos/600/400",
                "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
            ),
            MovieItem("2", "Inception", "A mind-bending thriller", "https://picsum.photos/601/400", "https://samplelib.com/lib/preview/mp4/sample-10s.mp4")
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(24.dp)) {
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
            .width(250.dp)
            .height(140.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable(true),
        colors = CardDefaults.cardColors(
            containerColor = if (focused) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.thumbnailUrl),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


