package com.pascal.movieku_compose.domain.usecase

import androidx.paging.PagingData
import com.pascal.movieku_compose.data.remote.dtos.Movie
import com.pascal.movieku_compose.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUC @Inject constructor(private val remoteRepository: IRepository) {
    fun execute(params: Params): Flow<PagingData<Movie>> {
        return remoteRepository.getMovies(selection = params.selection)
    }

    class Params(val numPage: Int? = null, val selection: Int)
}