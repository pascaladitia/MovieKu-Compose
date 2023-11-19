package com.pascal.movieku_compose.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.domain.usecase.GetMoviesUC
import com.pascal.movieku_compose.domain.usecase.GetSingleMovieUC
import com.pascal.movieku_compose.domain.usecase.UpdateFavorites
import com.pascal.movieku_compose.domain.model.MovieDetailInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUC,
    private val getSingleMovieUseCase: GetSingleMovieUC,
    private val updateFavMovieUC: UpdateFavorites,
): ViewModel() {
    var pageFlow by mutableStateOf<Flow<PagingData<Movie>>>(flowOf())
        private set

    private var _moviesList = MutableStateFlow(PagingData.empty<Movie>())
    val moviesList: StateFlow<PagingData<Movie>> get() = _moviesList

    private val _movie = MutableStateFlow<MovieDetailInfo?>(null)
    val movie: StateFlow<MovieDetailInfo?> = _movie

    var selection: Int = 0
        private set

    init {
        getMovies(selection)
    }

    fun resetFlow() {
        _moviesList =  MutableStateFlow(PagingData.empty())
    }

    fun getMovies(selection: Int) {
        viewModelScope.launch {
            pageFlow = getMoviesUseCase.execute(GetMoviesUC.Params(selection = selection)).cachedIn(this)
            pageFlow.collect { _moviesList.value = it }
        }
    }

    fun getMovies2(selection: Int): Flow<PagingData<Movie>> {
        return getMoviesUseCase.execute(GetMoviesUC.Params(selection = selection)).cachedIn(viewModelScope)
    }

    suspend fun suspendGetSingleMovie(id: Int): MovieDetailInfo {
       return getSingleMovieUseCase.execute(GetSingleMovieUC.Params(id))
    }

    fun UpdateFavMovie(item: FavoritesItem, favChecked: Boolean) {
        viewModelScope.launch {
            updateFavMovieUC.execute(UpdateFavorites.Params(item, favChecked))
        }
    }
}