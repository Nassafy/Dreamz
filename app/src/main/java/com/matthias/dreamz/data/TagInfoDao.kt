package com.matthias.dreamz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.matthias.dreamz.data.model.TagInfo
import com.matthias.dreamz.data.model.TagType
import kotlinx.coroutines.flow.Flow

@Dao
interface TagInfoDao {
    @Insert
    fun addTagInfo(vararg tagInfo: TagInfo)

    @Query("select * from TagInfo where tagType=:tagType order by count desc")
    fun getTagsInfo(tagType: TagType): Flow<List<TagInfo>>

    @Query("delete from TagInfo")
    fun deleteTagsInfo()
}