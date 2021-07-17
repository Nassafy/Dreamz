package com.matthias.dreamz.di

import android.content.Context
import com.matthias.dreamz.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideDreamDayDao(appDatabase: AppDatabase): DreamDayDao {
        return appDatabase.dreamDayDao()
    }

    @Provides
    fun provideDreamDao(appDatabase: AppDatabase): DreamDao {
        return appDatabase.dreamDao()
    }

    @Provides
    fun provideTagDao(appDatabase: AppDatabase): TagDao {
        return appDatabase.tagDao()
    }

    @Provides
    fun provideTagInfoDao(appDatabase: AppDatabase): TagInfoDao {
        return appDatabase.tagInfoDao()
    }
}