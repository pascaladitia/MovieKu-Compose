package com.pascal.movieku_compose.data.remote.dtos

data class AuthorDetails(
    val avatar_path: String,
    val name: String,
    val rating: Double,
    val username: String
)