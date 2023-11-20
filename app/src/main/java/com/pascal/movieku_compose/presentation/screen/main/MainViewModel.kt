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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUC,
    private val getSingleMovieUseCase: GetSingleMovieUC,
    private val updateFavMovieUC: UpdateFavorites,
): ViewModel() {

    fun getMovies(selection: Int): Flow<PagingData<Movie>> {
        return try {
            getMoviesUseCase.execute(GetMoviesUC.Params(selection = selection)).cachedIn(viewModelScope)
        } catch (e: Exception) {

            throw e
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