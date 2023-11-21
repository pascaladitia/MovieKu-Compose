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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUC,
    private val getSingleMovieUseCase: GetSingleMovieUC,
    private val updateFavMovieUC: UpdateFavorites,
): ViewModel() {

    private val _movies =  MutableStateFlow(PagingData.empty<Movie>())
    val movies: StateFlow<PagingData<Movie>> = _movies

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    suspend fun loadMovies(selection: Int) {
        try {
            getMoviesUseCase.execute(GetMoviesUC.Params(selection = selection))
                .cachedIn(viewModelScope)
                .collect {
                    _movies.value = it
                    _isLoading.value = false
                }
        } catch (e: Exception) {
            e.printStackTrace()
            _isLoading.value = false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun suspendGetSingleMovie(id: Int): MovieDetailInfo {
       return getSingleMovieUseCase.execute(GetSingleMovieUC.Params(id))
    }

    fun updateFavMovie(item: FavoritesItem, favChecked: Boolean) {
        viewModelScope.launch {
            updateFavMovieUC.execute(UpdateFavorites.Params(item, favChecked))
        }
    }
}