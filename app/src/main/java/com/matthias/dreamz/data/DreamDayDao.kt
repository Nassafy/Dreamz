package com.matthias.dreamz.data

import androidx.room.*
import com.matthias.dreamz.data.model.DreamDay
import com.matthias.dreamz.data.model.DreamDayWithDream
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDayDao {
    @Transaction
    @Query("select * from DreamDay order by date DESC")
    fun getDreamDaysWithDreams(): Flow<List<DreamDayWithDream>>

    @Query("select * from DreamDay order by date DESC")
    fun getDreamDays(): Flow<List<DreamDay>>

    @Query("select * from DreamDay where date >= :dateA and date < :dateB ")
    fun getDreamDayByDate(dateA: Long, dateB: Long): Flow<DreamDay>

    @Query("select * from DreamDay where date > :dateA and date < :dateB ")
    fun getDreamDaysByDate(dateA: Long, dateB: Long): Flow<List<DreamDay>>

    @Query("select count() from DreamDay")
    fun getDreamDaysCount(): Flow<Int>

    @Query("select * from DreamDay where uid=(:uid)")
    fun getDreamDayWithDream(uid: Long): Flow<DreamDayWithDream?>

    @Query("select * from DreamDay where uid=(:uid)")
    fun getDreamDay(uid: Long): Flow<DreamDay?>

    @Transaction
    @Insert
    suspend fun saveDreamDay(dreamDay: DreamDay): Long

    @Update
    suspend fun updateDreamDay(dreamDay: DreamDay)

    @Query("delete from DreamDay where uid=:dreamDayId")
    suspend fun deleteDreamDay(dreamDayId: Long)
}