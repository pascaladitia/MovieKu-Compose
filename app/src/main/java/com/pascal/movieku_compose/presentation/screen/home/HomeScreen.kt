package com.pascal.movieku_compose.presentation.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.presentation.component.MovieItemGrid
import com.pascal.movieku_compose.presentation.screen.main.MainViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: MainViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.loadMovies(0)
    }

    val movies: LazyPagingItems<Movie> = viewModel.movies.collectAsLazyPagingItems()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
        )
    } else {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = modifier.padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(130.dp),
                state = rememberLazyGridState()
            ) {
                items(count = movies.itemCount) { index ->
                    movies[index]?.let {
                        MovieItemGrid(it, onMovieClicked)
                    }
                }
            }
        }
    }
}