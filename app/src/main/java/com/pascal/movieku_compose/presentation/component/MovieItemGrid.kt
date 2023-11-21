package com.pascal.movieku_compose.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    val showShimmer = remember { mutableStateOf(true) }

    val model = remember {
        ImageRequest.Builder(context)
            .data(url)
            .size(Size.ORIGINAL)
            .crossfade(true)
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
        AsyncImage(
            model = model,
            contentDescription = item.title,
            modifier = Modifier
                .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value))
                .width(150.dp)
                .heightIn(min = 220.dp)
                .align(Alignment.Center),
            onSuccess = { showShimmer.value = false },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.8f),
            Color.LightGray.copy(alpha = 0.4f),
            Color.LightGray.copy(alpha = 0.8f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(0f, 0f),
            end = Offset(translateAnimation.value, translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}
