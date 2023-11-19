package com.pascal.movieku_compose.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.pascal.movieku_compose.R
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.domain.model.MovieDetailInfo
import com.pascal.movieku_compose.presentation.home.MainViewModel
import com.pascal.movieku_compose.presentation.ui.theme.MovieKuComposeTheme
import com.pascal.movieku_compose.utils.POSTER_BASE_URL
import com.pascal.movieku_compose.utils.W185
import com.pascal.movieku_compose.utils.W500
import com.pascal.movieku_compose.utils.YOUTUBE_TN_URL
import com.pascal.movieku_compose.utils.YOUTUBE_TRAILERS_URL
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    movieId: Int,
    onNavBack: () -> Unit
) {
    val movieDetailInfo by produceState<MovieDetailInfo?>(initialValue = null) {
        value = viewModel.suspendGetSingleMovie(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Preview") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavBack()
                        }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back Button")
                         }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        content = { paddingValues ->
            DetailContent(movieDetailInfo = movieDetailInfo, paddingValues = paddingValues, viewModel = viewModel)
        }
    )
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    movieDetailInfo: MovieDetailInfo?,
    paddingValues: PaddingValues,
    viewModel: MainViewModel?
) {
    if (movieDetailInfo == null) return
    
    var favBtnClicked by rememberSaveable {
        mutableStateOf(movieDetailInfo.favorite)
    }

    val myMovie = movieDetailInfo.movie
    val url = POSTER_BASE_URL + W500 + myMovie.backdrop_path
    val url2 = POSTER_BASE_URL + W185 + myMovie.poster_path
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = modifier.padding(paddingValues), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
            ) {
                val context = LocalContext.current
                val painter = remember {
                    ImageRequest.Builder(context)
                        .data(url)
                        .size(Size.ORIGINAL)
                        .crossfade(false)
                        .build()
                }
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }

            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .height(124.dp)
                        .width(86.dp)
                ) {
                    AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                        .data(url2)
                        .size(Size.ORIGINAL)
                        .crossfade(false)
                        .build(),
                        contentDescription ="",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp)
                    )
                }

                val relaseDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(myMovie.release_date)
                val reFormatDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(relaseDate)

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1.0f)
                ) {
                    Text(text = myMovie.title, overflow = TextOverflow.Ellipsis, maxLines = 2)
                    Text(text = reFormatDate)
                    RatingBar(
                        modifier = Modifier.height(20.dp),
                        stars = 10,
                        rating = myMovie.vote_average,
                        starsColor = Color.Red
                    )
                    Text(text = "${myMovie.vote_count}/10")
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(25.dp)
                        .width(25.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                color = Color.Red
                            )
                        ) {
                            favBtnClicked = !favBtnClicked
                            viewModel?.UpdateFavMovie(
                                FavoritesItem(myMovie.id, myMovie.poster_path),
                                favBtnClicked
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            if (favBtnClicked)
                                R.drawable.ic_favorite_red_24dp
                            else
                                R.drawable.ic_favorite_border_white_24dp
                        ),
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .clip(CircleShape)
                    )
                }

                Box(
                    Modifier
                        .width(8.dp)
                        .fillMaxHeight(1f))
            }

            Text(
                text = myMovie.overview,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
            )

            if (movieDetailInfo.videos.isNotEmpty()) {
                Text(
                    text = "Trailers",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            shape = CircleShape
                        )
                )
            } else {
                Text(
                    "No available trailers",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            shape = CircleShape
                        )
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }

            val context = LocalContext.current
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieDetailInfo.videos) {
                    val urlThumbnail = YOUTUBE_TN_URL + it.key + "/hqdefault.jpg"
                    val painter = ImageRequest.Builder(LocalContext.current)
                        .data(urlThumbnail)
                        .size(Size.ORIGINAL)
                        .build()

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.clickable {
                            val trailerUrl = YOUTUBE_TRAILERS_URL + it.key
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                            ContextCompat.startActivity(context, i, null)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_svg),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .alpha(1f)
                                .zIndex(10f)
                                .scale(1.5f)
                                .alpha(0.4f)
                        )
                        AsyncImage(
                            model = painter,
                            contentDescription = "",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .height(100.dp)
                                .zIndex(0f)
                        )
                    }
                }
            }

            if (movieDetailInfo.review.isNotEmpty()) {
                Text(
                    "Reviews",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer, shape = CircleShape)

                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(8.dp))
            ) {
                for (i in (0..movieDetailInfo.review.size - 1)) {
                    Text(
                        text = movieDetailInfo.review[i].content,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable {
                                val reviewsUrl = movieDetailInfo.review[i].url;
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reviewsUrl))
                                ContextCompat.startActivity(context, intent, null)
                            }
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewDetail() {
    MovieKuComposeTheme {
        DetailContent(
            movieDetailInfo = MovieDetailInfo(
                emptyList(),
                emptyList(),
                Movie(
                    true,
                    "",
                    emptyList(),
                    12,
                    "",
                    "hello",
                    "a little brief description of the content of the movie, lorem ipsum dolor samet",
                    10.0,
                    "",
                    "2023-10-10",
                    "hello",
                    false,
                    5.4,
                    12,
                ),
                true
            ),
            paddingValues = PaddingValues(all = 0.dp),
            viewModel = null
        )
    }
}