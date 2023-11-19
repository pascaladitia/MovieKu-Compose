package com.pascal.movieku_compose.di

object EnvironmentConfig {
    const val BASE_DOMAIN = "api.themoviedb.org/3/movie"
    const val BASE_URL = "https://$BASE_DOMAIN/"
    val allowedSSlFingerprints = emptyList<String>()
}