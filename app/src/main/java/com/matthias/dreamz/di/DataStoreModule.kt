package com.matthias.dreamz.di

import android.content.Context
import com.matthias.dreamz.datastore.FilterDataStoreManager
import com.matthias.dreamz.datastore.FlagDataStoreManager
import com.matthias.dreamz.datastore.SettingsDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStoreManager {
        return SettingsDataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideFilterDataStoreManager(@ApplicationContext context: Context): FilterDataStoreManager {
        return FilterDataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideFlagDataStoreManager(@ApplicationContext context: Context): FlagDataStoreManager {
        return FlagDataStoreManager(context)
    }
}