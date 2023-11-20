package com.pascal.movieku_compose.presentation.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home")
    object PopularScreen: Screen("popular")
    object FavoriteScreen: Screen("favorite")
    object DetailScreen: Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}