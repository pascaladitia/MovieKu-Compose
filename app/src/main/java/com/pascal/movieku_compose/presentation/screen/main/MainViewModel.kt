package com.pascal.movieku_compose.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.domain.model.MovieDetailInfo
import com.pascal.movieku_compose.domain.usecase.GetMoviesUC
import com.pascal.movieku_compose.domain.usecase.GetSingleMovieUC
import com.pascal.movieku_compose.domain.usecase.UpdateFavorites
import com.pascal.movieku_compose.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUC,
    private val getSingleMovieUseCase: GetSingleMovieUC,
    private val updateFavMovieUC: UpdateFavorites,
) : ViewModel() {

    private val _movies = MutableStateFlow(PagingData.empty<Movie>())
    val movies: StateFlow<PagingData<Movie>> = _movies

    private val _movieDetailUiState = MutableStateFlow<UiState<MovieDetailInfo?>>(UiState.Loading)
    val movieDetailUiState: StateFlow<UiState<MovieDetailInfo?>> = _movieDetailUiState


    suspend fun loadMovies(selection: Int) {
        getMoviesUseCase.execute(GetMoviesUC.Params(selection = selection))
            .cachedIn(viewModelScope)
            .collect {
                _movies.value = it
            }
    }

    suspend fun loadDetailMovie(id: Int) {
        try {
            val result = getSingleMovieUseCase.execute(GetSingleMovieUC.Params(id))
            _movieDetailUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _movieDetailUiState.value = UiState.Error(e)
        }
    }
    fun updateFavMovie(item: FavoritesItem, favChecked: Boolean) {
        viewModelScope.launch {
            updateFavMovieUC.execute(UpdateFavorites.Params(item, favChecked))
        }
    }
}