package com.matthias.dreamz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matthias.dreamz.data.adapter.LocalDateConverter
import com.matthias.dreamz.data.adapter.LocalDateTimeConverter
import com.matthias.dreamz.data.adapter.StringArrayConverter
import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.data.model.DreamDay
import com.matthias.dreamz.data.model.Tag
import com.matthias.dreamz.data.model.TagInfo

@Database(
    entities = [
        DreamDay::class,
        Dream::class,
        Tag::class,
        TagInfo::class
    ], version = 7
)
@TypeConverters(
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    StringArrayConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dreamDayDao(): DreamDayDao

    abstract fun dreamDao(): DreamDao

    abstract fun tagDao(): TagDao

    abstract fun tagInfoDao(): TagInfoDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "dreamz")
                .fallbackToDestructiveMigration(
                )
                .build()
        }


    }
}