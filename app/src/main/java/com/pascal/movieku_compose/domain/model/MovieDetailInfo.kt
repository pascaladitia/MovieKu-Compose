package com.pascal.movieku_compose.domain.model

import com.pascal.movieku_compose.data.remote.dtos.*

class MovieDetailInfo(
    val review: List<Review>,
    val videos: List<Video>,
    val movie: Movie,
    var favorite: Boolean
)