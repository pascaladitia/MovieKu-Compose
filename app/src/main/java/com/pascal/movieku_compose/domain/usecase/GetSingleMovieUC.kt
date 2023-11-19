package com.pascal.movieku_compose.domain.usecase

import com.pascal.movieku_compose.domain.model.MovieDetailInfo
import com.pascal.movieku_compose.domain.repository.IRepository
import javax.inject.Inject

class GetSingleMovieUC @Inject constructor(private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): MovieDetailInfo {
        val movie   = remoteRepository.getSingleMovie(params.id)
        val videos  = remoteRepository.getMovieVideos(params.id)
        val reviews = remoteRepository.getMovieReviews(params.id)
        val favorite = remoteRepository.getFavoriteStatus(params.id)
        return MovieDetailInfo(
            reviews.results,
            videos.results,
            movie,
            favorite
        )
    }

    class Params(val id: Int)
}