package com.pascal.movieku_compose.data.local

import com.pascal.movieku_compose.di.AppMainDB
import com.pascal.movieku_compose.data.local.database.AppDatabase
import com.pascal.movieku_compose.data.local.model.FavoritesItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(@AppMainDB private val database: AppDatabase) {

    suspend fun storeFavoriteItem(favoritesItem: FavoritesItem) {
        database.favoritesDao().insertFavorite(favoritesItem)
    }

    suspend fun deleteFavoriteItem(favoritesItem: FavoritesItem) {
        database.favoritesDao().deleteFavorite(favoritesItem)
    }

    suspend fun getFavoriteMovies(): List<FavoritesItem>? {
        return database.favoritesDao().getFavoriteMovieList()
    }

    suspend fun getFavoriteMovie(id: Int): Boolean {
        return (database.favoritesDao().getFavoriteMovie(id) != null)
    }

}