package com.pascal.movieku_compose.di

import android.content.Context
import androidx.room.Room
import com.pascal.movieku_compose.data.local.LocalDataSource
import com.pascal.movieku_compose.data.local.database.AppDatabase
import com.pascal.movieku_compose.data.local.database.DatabaseConstants
import com.pascal.movieku_compose.data.remote.AppService
import com.pascal.movieku_compose.data.setup.AppServiceFactory
import com.pascal.movieku_compose.data.setup.HttpClientFactory
import com.pascal.movieku_compose.data.setup.ServiceFactory
import com.pascal.movieku_compose.di.EnvironmentConfig.BASE_DOMAIN
import com.pascal.movieku_compose.di.EnvironmentConfig.allowedSSlFingerprints
import com.pascal.movieku_compose.domain.repository.IRepository
import com.pascal.movieku_compose.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DIServiceProvider {
    @Singleton
    @AppMainDB
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DatabaseConstants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClientFactory(): HttpClientFactory {
        return HttpClientFactory(BASE_DOMAIN, allowedSSlFingerprints)
    }

    @Singleton
    @Provides
    fun provideServiceFactory(): ServiceFactory {
        return ServiceFactory(EnvironmentConfig.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideService(httpClientFactory: HttpClientFactory, serviceFactory : ServiceFactory): AppService {
        return AppServiceFactory(httpClientFactory).getAppService(serviceFactory)
    }

    @Singleton
    @Provides
    fun provideRepository(appService: AppService, localDataSource: LocalDataSource): IRepository {
        return Repository(appService, localDataSource)
    }
}
