package com.matthias.dreamz.di

import com.matthias.dreamz.api.AuthApi
import com.matthias.dreamz.api.DreamApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideDreamService(retrofit: Retrofit): DreamApi {
        return retrofit.create(DreamApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}