package com.pascal.movieku_compose.domain.usecase

import com.pascal.movieku_compose.data.local.model.FavoritesItem
import com.pascal.movieku_compose.domain.repository.IRepository
import javax.inject.Inject

class UpdateFavorites @Inject constructor(private val repository: IRepository) {
    suspend fun execute(params: Params) {
        repository.updateFavorite(params.item, params.checked)
    }
    class Params(val item: FavoritesItem, val checked: Boolean)
}