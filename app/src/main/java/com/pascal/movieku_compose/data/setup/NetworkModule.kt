package com.pascal.movieku_compose.data.setup

import com.pascal.movieku_compose.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object NetworkModule {

    fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    fun basicHeaderInterceptor() = Interceptor {
        val req = it.request()
        val query = req.url
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.IMDB_API_KEY)
            .build()
        it.proceed(req.newBuilder().url(query).build())
    }

}