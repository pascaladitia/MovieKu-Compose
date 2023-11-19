package com.pascal.movieku_compose.data.remote.dtos

data class VideoCatalog(
    val id: Int,
    val results: List<Video>
)