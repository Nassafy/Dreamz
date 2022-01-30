package com.matthias.dreamz.ui.screen.dreamlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.data.model.DreamDay
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.datastore.FilterDataStoreManager
import com.matthias.dreamz.datastore.FlagDataStoreManager
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DreamListViewModel @Inject constructor(
    private val dreamRepository: DreamRepository,
    private val filterDataStoreManager: FilterDataStoreManager,
    private val workManager: WorkManager,
    flagDataStoreManager: FlagDataStoreManager
) :
    ViewModel() {
    val dreamDays =
        dreamRepository.getDreamDaysWithDreams().map { dreamDays ->
            dreamDays.map { it.toState() }
        }

    val todayDream = dreamRepository.getTodayDreamDay()

    val refreshing = isRefreshing()

    val syncState = flagDataStoreManager.syncState


    fun addDream(onAdd: (dreamDayId: Long) -> Unit) {
        viewModelScope.launch {
            val id = dreamRepository.saveDreamDay(DreamDay(date = LocalDate.now()))
            dreamRepository.addDream(Dream(dreamDayId = id))
            onAdd(id)
        }
    }

    fun sync() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val work = OneTimeWorkRequestBuilder<SyncWorker>().setConstraints(
            constraints
        ).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).addTag(SyncWorker.TAG)
            .build()
        workManager.enqueueUniqueWork(SyncWorker.NAME_UNIQUE, ExistingWorkPolicy.KEEP, work)
    }

    fun getSuggestions(tagType: TagType): Flow<List<String>> {
        return this.dreamRepository.getTags(tagType).map { tags -> tags.map { it.tag }.sorted() }
    }

    val filterText = filterDataStoreManager.text

    fun setFilterText(text: String) {
        viewModelScope.launch {
            filterDataStoreManager.setText(text)
        }
    }

    val filterTag = filterDataStoreManager.tag

    fun setFilterTag(tag: String) {
        viewModelScope.launch {
            filterDataStoreManager.setTag(tag)
        }
    }

    val filterPeople = filterDataStoreManager.people

    fun setFilterPeople(people: String) {
        viewModelScope.launch {
            filterDataStoreManager.setPeople(people)
        }
    }

    val filterMinNote = filterDataStoreManager.minNote

    fun setFilterMinNote(minNote: Int?) {
        viewModelScope.launch {
            filterDataStoreManager.setMinNote(minNote)
        }
    }

    val filterMaxNote = filterDataStoreManager.maxNote

    fun setFilterMaxNote(maxNote: Int?) {
        viewModelScope.launch {
            filterDataStoreManager.setMaxNote(maxNote)
        }
    }

    private fun isRefreshing(): LiveData<Boolean> {
        val source1 = workManager.getWorkInfosForUniqueWorkLiveData(SyncWorker.NAME_PERIODIC)
        val source2 = workManager.getWorkInfosForUniqueWorkLiveData(SyncWorker.NAME_UNIQUE)
        val mediator = MediatorLiveData<Pair<List<WorkInfo>, List<WorkInfo>>>()
        mediator.apply {
            addSource(source1) {
                value = Pair(it ?: listOf(), value?.second ?: listOf())

            }
            addSource(source2) {
                value = Pair(value?.first ?: listOf(), it ?: listOf())
            }
        }
        return map(mediator) { pair ->
            pair.first.any {
                it.state == WorkInfo.State.RUNNING
            } || pair.second.any {
                it.state == WorkInfo.State.RUNNING
            }
        }
    }
}