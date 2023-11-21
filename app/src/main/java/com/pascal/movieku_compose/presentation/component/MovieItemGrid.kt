package com.pascal.movieku_compose.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.pascal.movieku_compose.R
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.utils.POSTER_BASE_URL
import com.pascal.movieku_compose.utils.W185

@Composable
fun MovieItemGrid(item: Movie, onMovieClicked: (Int) -> Unit) {
    val url: String = POSTER_BASE_URL + W185 + item.poster_path
    val context = LocalContext.current

    val model = remember {
        ImageRequest.Builder(context)
            .data(url)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .placeholder(R.drawable.no_image)
            .error(R.drawable.no_image)
            .build()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMovieClicked.invoke(item.id)
            },
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            loading = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    CircularProgressIndicator()
                }
            },
            model = model,
            contentDescription = item.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface) // Background color
        )
    }
}
