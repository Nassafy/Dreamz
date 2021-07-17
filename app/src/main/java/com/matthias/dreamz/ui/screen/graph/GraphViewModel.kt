package com.matthias.dreamz.ui.screen.graph

import androidx.lifecycle.ViewModel
import com.matthias.dreamz.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(private val dreamRepository: DreamRepository) :
    ViewModel() {
    fun getDreams(year: Int): Flow<HashMap<Int, Int>> {
        val start = LocalDate.of(year, 1, 1)
        val end = LocalDate.of(year + 1, 1, 1)
        return dreamRepository.getDreamDayByDate(start, end).map { dreamDays ->
            val dreamDaysMap = hashMapOf<Int, Int>()
            for (i in 1..12) {
                dreamDaysMap[i] = dreamDays.filter {
                    it.date.monthValue == i
                }.count()
            }
            dreamDaysMap
        }
    }

}