package com.pascal.movieku_compose.domain.repository

import androidx.paging.PagingData
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.data.remote.dtos.ReviewsCatalog
import com.pascal.movieku_compose.data.remote.dtos.VideoCatalog
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getMoviesCatalog(numPage:Int?): List<Movie>
    fun getMovies(selection: Int): Flow<PagingData<Movie>>
    suspend fun getSingleMovie(id: Int): Movie
    suspend fun getMovieVideos(id: Int): VideoCatalog
    suspend fun getMovieReviews(id: Int): ReviewsCatalog
    suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean)
    suspend fun getFavoriteStatus(movieId: Int): Boolean
}