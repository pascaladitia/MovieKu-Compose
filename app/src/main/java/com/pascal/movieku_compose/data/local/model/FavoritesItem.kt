package com.pascal.movieku_compose.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pascal.movieku_compose.data.local.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_FAVORITES)
data class FavoritesItem (
    @PrimaryKey
    val id: Int,
    val posterPath: String,
)
