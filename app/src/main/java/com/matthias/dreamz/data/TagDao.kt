package com.matthias.dreamz.data

import androidx.room.*
import com.matthias.dreamz.data.model.Tag
import com.matthias.dreamz.data.model.TagType
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("select * from Tag where tagType=(:type)")
    fun getTags(type: TagType): Flow<List<Tag>>

    @Query("select * from Tag where tagType=(:type) and tag=(:name)")
    fun getTagByName(type: TagType, name: String): Tag?

    @Insert
    fun addTag(tag: Tag)

    @Update
    fun updateTag(tag: Tag)

    @Query("delete from Tag")
    fun deleteAllTags()

    @Delete
    fun deleteTag(tag: Tag)
}