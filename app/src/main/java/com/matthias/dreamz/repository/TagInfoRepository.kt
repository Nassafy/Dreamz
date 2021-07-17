package com.matthias.dreamz.repository

import com.matthias.dreamz.data.DreamDao
import com.matthias.dreamz.data.TagInfoDao
import com.matthias.dreamz.data.model.TagInfo
import com.matthias.dreamz.data.model.TagType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class TagInfoRepository @Inject constructor(
    private val tagInfoDao: TagInfoDao,
    private val dreamDao: DreamDao,
) {
    fun getTagsInfo(tagType: TagType) = tagInfoDao.getTagsInfo(tagType)

    suspend fun reindexTagInfo() {
        tagInfoDao.deleteTagsInfo()
        TagType.values().forEach {
            addTagInfo(it)
        }
    }

    private suspend fun addTagInfo(tagType: TagType) {
        val tags = hashMapOf<String, Int>()
        dreamDao.getDreams().take(1).collect { dreams ->
            dreams.forEach { dream ->
                val tagList = when (tagType) {
                    TagType.PEOPLE -> dream.dreamMetadata.peoples
                    TagType.TAG -> dream.dreamMetadata.tags
                }

                tagList.forEach {
                    tags[it] = (tags[it] ?: 0) + 1
                }
            }
            tagInfoDao.addTagInfo(*tags.entries.map {
                TagInfo(tagType = tagType, name = it.key, count = it.value)
            }.toTypedArray())
        }
    }
}