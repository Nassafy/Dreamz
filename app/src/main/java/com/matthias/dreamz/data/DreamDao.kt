package com.matthias.dreamz.data

import androidx.room.*
import com.matthias.dreamz.data.model.Dream
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Query("select * from dream")
    fun getDreams(): Flow<List<Dream>>

    @Query("select count() from dream")
    fun getDreamsCount(): Flow<Int>

    @Query("select * from dream where uid=(:dreamId)")
    suspend fun getDream(dreamId: Long): Dream?

    @Insert
    suspend fun saveDream(vararg dream: Dream)

    @Update
    suspend fun updateDream(vararg dream: Dream)

    @Query("delete from Dream where uid=(:dreamId)")
    suspend fun deleteDream(dreamId: Long)
}