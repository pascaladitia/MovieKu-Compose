package com.pascal.movieku_compose.presentation.screen.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.presentation.component.MovieItemGrid
import com.pascal.movieku_compose.presentation.component.ShimmerAnimation
import com.pascal.movieku_compose.presentation.screen.main.MainViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: MainViewModel = hiltViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.loadMovies(2)
    }

    val movies: LazyPagingItems<Movie> = viewModel.movies.collectAsLazyPagingItems()

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

            when {
                movies.loadState.refresh is LoadState.Loading || movies.loadState.append is LoadState.Loading -> {
                    repeat(6) {
                        item {
                            ShimmerAnimation()
                        }
                    }
                }

                movies.loadState.refresh is LoadState.Error || movies.loadState.append is LoadState.Error -> {
                    //handle error
                }
            }
        }
    }
}