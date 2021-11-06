package com.matthias.dreamz.repository

import com.matthias.dreamz.api.DreamApi
import com.matthias.dreamz.api.DreamDayDto
import com.matthias.dreamz.data.DreamDao
import com.matthias.dreamz.data.DreamDayDao
import com.matthias.dreamz.data.TagDao
import com.matthias.dreamz.data.model.*
import com.matthias.dreamz.datastore.FilterDataStoreManager
import com.matthias.dreamz.toDto
import com.matthias.dreamz.toModel
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

class DreamRepository @Inject constructor(
    private val dreamApi: DreamApi,
    private val dreamDayDao: DreamDayDao,
    private val dreamDao: DreamDao,
    private val tagDao: TagDao,
    private val filterManager: FilterDataStoreManager
) {
    private suspend fun getDistantDreamDays(): Flow<List<DreamDayDto>> {
        return flow {
            val dreamDays = dreamApi.getDreams().results
            emit(dreamDays)
        }
    }

    fun getDreamDay(dreamId: Long) = dreamDayDao.getDreamDayWithDream(dreamId)

    fun getTodayDreamDay(): Flow<DreamDay?> {
        val start =
            LocalDate.now().atStartOfDay().toInstant(OffsetDateTime.now().offset).toEpochMilli()
        val end = LocalDate.now().atStartOfDay().plusDays(1).toInstant(OffsetDateTime.now().offset)
            .toEpochMilli()

        return dreamDayDao.getDreamDayByDate(start, end)
    }

    fun getDreamDayByDate(start: LocalDate, end: LocalDate): Flow<List<DreamDay>> {
        val startEpoch = start.atStartOfDay().toInstant(OffsetDateTime.now().offset).toEpochMilli()
        val endEpoch =
            end.plusDays(1).atStartOfDay().toInstant(OffsetDateTime.now().offset).toEpochMilli()
        return dreamDayDao.getDreamDaysByDate(startEpoch, endEpoch)
    }

    fun getCountOfDream() = dreamDao.getDreamsCount()

    fun getNumberOfDreamDay() = dreamDayDao.getDreamDaysCount()

    suspend fun saveDreamDay(dreamDay: DreamDay) = dreamDayDao.saveDreamDay(dreamDay)

    suspend fun updateDreamDay(dreamDay: DreamDay) = dreamDayDao.updateDreamDay(dreamDay)

    fun getAllDreamDaysWithDreams(): Flow<List<DreamDayWithDream>> =
        dreamDayDao.getDreamDaysWithDreams()

    fun getDreamDaysWithDreams(): Flow<List<DreamDayWithDream>> {
        return dreamDayDao.getDreamDaysWithDreams().combine(filterManager.text) { dreams, text ->
            dreams.filter { dreamDayWithDream ->
                text == null || text.isBlank() || dreamDayWithDream.dreams.any {
                    it.text.contains(text, ignoreCase = true) || it.name.contains(
                        text,
                        ignoreCase = true
                    )
                }
            }
        }.combine(filterManager.tag) { dreams, tag ->
            dreams.filter { dreamDayWithDream ->
                tag == null || tag.isBlank() || dreamDayWithDream.dreams.any {
                    it.dreamMetadata.tags.contains(tag)
                }
            }
        }.combine(filterManager.people) { dreams, people ->
            dreams.filter { dreamDayWithDream ->
                people == null || people.isBlank() || dreamDayWithDream.dreams.any {
                    it.dreamMetadata.peoples.contains(people)
                }
            }
        }
    }


    fun getDreamDays() = dreamDayDao.getDreamDays()

    suspend fun addDream(dream: Dream) = dreamDao.saveDream(dream)

    suspend fun saveDream(vararg dream: Dream) {
        for (d in dream) {
            dreamDayDao.getDreamDay(d.dreamDayId).take(1).collect {
                if (it != null) {
                    dreamDayDao.updateDreamDay(
                        it.copy(
                            technicalMetadata = it.technicalMetadata.copy(
                                lastChange = LocalDateTime.now()
                            )
                        )
                    )
                }
            }
            dreamDao.updateDream(d)
        }
    }

    suspend fun getDream(dreamId: Long) = dreamDao.getDream(dreamId)

    suspend fun deleteDream(dreamId: Long) = dreamDao.deleteDream(dreamId)

    suspend fun deleteDreamDay(dreamDayId: Long) = dreamDayDao.deleteDreamDay(dreamDayId)

    suspend fun syncDream() {
        getDistantDreamDays().combine(getAllDreamDaysWithDreams()) { distants, locals ->
            distants.forEach { distant ->
                val local = locals.find { it.dreamDay.id == distant.id }
                if (local == null || Duration.between(
                        local.dreamDay.technicalMetadata.lastChange?.toInstant(OffsetDateTime.now().offset),
                        distant.techMetadata.lastChange?.toInstant(OffsetDateTime.now().offset),
                    ).seconds > 5
                ) {
                    local?.dreamDay?.let { dreamDayDao.deleteDreamDay(it.uid) }
                    val dreamDayId = dreamDayDao.saveDreamDay(distant.toModel())
                    local?.dreams?.forEach { dreamDao.deleteDream(it.uid) }
                    distant.dreams.forEach {
                        dreamDao.saveDream(it.toModel(dreamDayId = dreamDayId))
                    }
                }
            }
            locals.forEach { local ->
                val distant = distants.find { it.id == local.dreamDay.id }
                if (distant == null || Duration.between(
                        distant.techMetadata.lastChange?.toInstant(OffsetDateTime.now().offset),
                        local.dreamDay.technicalMetadata.lastChange?.toInstant(OffsetDateTime.now().offset)
                    ).seconds > 5
                ) {
                    dreamApi.saveDream(local.toDto())
                }
            }
        }.first()
    }

    suspend fun syncTags() {
        tagDao.deleteAllTags()
        dreamDao.getDreams().take(1).collect {
            it.forEach { dream ->
                dream.dreamMetadata.peoples.forEach {
                    addTag(TagType.PEOPLE, it)
                }
                dream.dreamMetadata.tags.forEach {
                    addTag(TagType.TAG, it)
                }
            }
        }
    }

    fun getTags(type: TagType): Flow<List<Tag>> {
        return this.tagDao.getTags(type = type)
    }

    private fun addTag(type: TagType, tagValue: String) {
        val tag = tagDao.getTagByName(type, tagValue)
        if (tag == null) {
            tagDao.addTag(Tag(tagType = type, tag = tagValue, quantity = 1, uid = 0))
        } else {
            tagDao.updateTag(tag.copy(quantity = tag.quantity + 1))
        }
    }

}