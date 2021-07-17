package com.matthias.dreamz.ui.screen.editdream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDreamViewModel @Inject constructor(private val dreamRepository: DreamRepository) :
    ViewModel() {
    fun getDream(dreamId: Long) = dreamRepository.getDreamDay(dreamId).map { it?.toState() }

    fun addDream(dreamDayId: Long) {
        val dream = Dream(dreamDayId = dreamDayId)
        viewModelScope.launch() {
            dreamRepository.addDream(dream)
        }
    }

    fun saveDream(dream: DreamState) {
        viewModelScope.launch() {
            val d = dreamRepository.getDream(dream.id)
            if (d != null) {
                val updated = d.copy(
                    text = dream.text,
                    name = dream.name,
                    dreamMetadata = d.dreamMetadata.copy(lucid = dream.lucid)
                )
                if (updated != d) {
                    dreamRepository.saveDream(updated)
                }
            }

        }
    }

    fun deleteDream(dreamId: Long) {
        viewModelScope.launch() {
            dreamRepository.deleteDream(dreamId)
        }
    }

    fun deleteDreamDay(dreamDayId: Long) {
        viewModelScope.launch() {
            dreamRepository.deleteDreamDay(dreamDayId)
        }
    }
}