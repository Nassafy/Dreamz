package com.matthias.dreamz.ui.screen.viewdream

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.matthias.dreamz.data.model.DreamMetadata
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.datastore.FilterDataStoreManager
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.worker.TagSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewDreamViewModel @Inject constructor(
    private val dreamRepository: DreamRepository,
    filterDataStoreManager: FilterDataStoreManager,
    private val workManager: WorkManager
) :
    ViewModel() {

    private val title = mutableStateOf("")

    val searchText = filterDataStoreManager.text

    fun getDream(dreamId: Long) = dreamRepository.getDreamDay(dreamId).map {
        it?.toState()
    }.onEach { title.value = it?.date ?: "" }

    fun getDreams() = dreamRepository.getDreamDays()

    fun saveDreamMetadata(dreamId: Long, metadata: DreamMetadata) {
        viewModelScope.launch {
            val dream = dreamRepository.getDream(dreamId)
            if (dream != null) {
                dreamRepository.saveDream(dream.copy(dreamMetadata = metadata))
                TagSyncWorker.launch(workManager)
            }
        }

    }

    fun getSuggestions(tagType: TagType): Flow<List<String>> {
        return this.dreamRepository.getTags(tagType).map { tags -> tags.map { it.tag }.sorted() }
    }

}