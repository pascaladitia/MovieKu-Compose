package com.pascal.movieku_compose.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pascal.movieku_compose.data.local.LocalDataSource
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.data.remote.AppService
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.data.remote.dtos.ReviewsCatalog
import com.pascal.movieku_compose.data.remote.dtos.VideoCatalog
import com.pascal.movieku_compose.domain.pagination.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class Repository @Inject constructor(private val appService: AppService, private val localDataSource: LocalDataSource): IRepository {

    override suspend fun getMoviesCatalog(numPage: Int?): List<Movie> {
        return appService.getMovies(numPage).body()?.results ?: emptyList()
    }

    override fun getMovies(selection: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MoviesPagingSource(appService, localDataSource, selection)
            }
        ).flow
    }

    override suspend fun getSingleMovie(id: Int): Movie {
        return appService.getSingleMovie(id).body()!!
    }

    override suspend fun getMovieVideos(id: Int): VideoCatalog {
        return appService.getVideos(id).body()!!
    }

    override suspend fun getMovieReviews(id: Int): ReviewsCatalog {
        return appService.getReviews(id).body()!!
    }

    override suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean) {
        if (checkFav) {
            localDataSource.storeFavoriteItem(item)
        } else {
            localDataSource.deleteFavoriteItem(item)
        }
    }
    override suspend fun getFavoriteStatus(movieId: Int): Boolean {
        return localDataSource.getFavoriteMovie(movieId)
    }
}