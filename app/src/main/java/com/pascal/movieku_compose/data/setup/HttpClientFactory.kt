package com.pascal.movieku_compose.data.setup

import com.pascal.movieku_compose.BuildConfig
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpClientFactory @Inject constructor(private val baseDomain: String, private val certificatesPins: List<String>) {
    val abstractClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)

        if (certificatesPins.size!=0) builder.certificatePinner(createCertificatePinner())

        if (BuildConfig.DEBUG)
            builder.addNetworkInterceptor(NetworkModule.createHttpLoggingInterceptor())

        builder.build()
    }

    private fun createCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder().apply {
            certificatesPins.forEach { this.add(baseDomain, it) }
        }.build()
    }

}