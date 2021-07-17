package com.matthias.dreamz.ui.screen.calendar

import androidx.lifecycle.ViewModel
import com.matthias.dreamz.data.model.Dream
import com.matthias.dreamz.data.model.DreamDay
import com.matthias.dreamz.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class CalendarViewModel @Inject constructor(private val dreamRepository: DreamRepository) :
    ViewModel() {
    fun getDreams(year: Int, month: Int): Flow<HashMap<LocalDate, DreamDay>> {
        val start = LocalDate.of(year, month, 1)
        val end = LocalDate.of(year, month, 1).plusMonths(1)
        return dreamRepository.getDreamDayByDate(start, end).map { dreamDays ->
            val hash = hashMapOf<LocalDate, DreamDay>()
            dreamDays.forEach {
                hash[it.date] = it
            }
            hash
        }
    }
}