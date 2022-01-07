package com.matthias.dreamz.ui.screen.editdream

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.api.DreamApi
import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditDreamViewModel @Inject constructor(private val dreamRepository: DreamRepository, private val dreamApi: DreamApi) :
    ViewModel() {
    fun getDream(dreamId: Long) = dreamRepository.getDreamDay(dreamId).map { it?.toState() }

    fun addDream(dreamDayId: Long) {
        val dream = Dream(dreamDayId = dreamDayId)
        viewModelScope.launch {
            dreamRepository.addDream(dream)
        }
    }

    fun saveDream(dream: DreamState) {
        viewModelScope.launch {
            val d = dreamRepository.getDream(dream.id)
            if (d != null) {
                val updated = d.copy(
                    text = dream.text,
                    name = dream.name,
                    textNote = dream.textNote,
                    dreamMetadata = d.dreamMetadata.copy(lucid = dream.lucid)
                )
                if (updated != d) {
                    dreamRepository.saveDream(updated)
                }
            }

        }
    }

    fun deleteDream(dreamId: Long) {
        viewModelScope.launch {
            dreamRepository.deleteDream(dreamId)
        }
    }

    fun deleteDreamDay(dreamDayUuid: String, dreamDayId: Long, afterDelete: () -> Unit) {
        viewModelScope.launch {
            try {
                dreamApi.deleteDream(dreamDayUuid)
                dreamRepository.deleteDreamDay(dreamDayId)
                afterDelete()

            } catch (error: Exception) {
                Log.d("Dreamz", "deleteDreamDay: ${error.message}")
            }
        }
    }
}