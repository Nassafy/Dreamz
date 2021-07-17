package com.matthias.dreamz.ui.screen.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalPagerApi
@Composable()
fun CalendarScreen(calendarViewModel: CalendarViewModel) {
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE - 1)

    Scaffold(topBar = { TopAppBar(title = { Text("Calendar") }) }) {
        HorizontalPager(state = pagerState, count = Int.MAX_VALUE) { page ->
            Calendar(calendarViewModel = calendarViewModel, monthOffset = Int.MAX_VALUE - 1 - page)

        }
    }
}

@Composable()
fun Calendar(calendarViewModel: CalendarViewModel, monthOffset: Int) {
    val pageDate = LocalDate.now().minusMonths(monthOffset.toLong())

    val dreams =
        calendarViewModel.getDreams(pageDate.year, pageDate.monthValue)
            .collectAsState(initial = hashMapOf()).value
    val firstDay = LocalDate.of(pageDate.year, pageDate.month, 1)
    val nbDays = firstDay.plusMonths(1).minusDays(1).dayOfMonth
    val nbWeek = nbDays / 7
    val firstDayOfWeek = firstDay.dayOfWeek.value - 1
    val currentDay =
        if (firstDay.month == LocalDate.now().month && firstDay.year == LocalDate.now().year)
            LocalDate.now().dayOfMonth
        else
            nbDays
    val daysLabel = listOf("L", "M", "M", "J", "V", "S", "D")
    val nbDream = dreams.values.size
    Column() {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                pageDate.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0..6) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(daysLabel[i], style = MaterialTheme.typography.h6)
                }
            }
        }
        for (i in 0..nbWeek + 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0..6) {
                    val day = i * 7 + j - (firstDayOfWeek - 1)
                    if (day <= 0 || day > nbDays) {
                        CalendarDay("N", DayState.INACTIVE)
                    } else {
                        val state =
                            if (day <= currentDay) {
                                val dream =
                                    dreams[firstDay.plusDays(day.toLong() - 1)]
                                if (dream != null)
                                    DayState.LUCID
                                else
                                    DayState.ACTIVE
                            } else {
                                DayState.FUTURE
                            }
                        CalendarDay("$day", state)
                    }
                }
            }
        }

        Text("Dream this month: $nbDream")
    }
}

@Composable
fun CalendarDay(text: String, state: DayState) {
    val elevation = 15.dp

    val color = when (state) {
        DayState.INACTIVE -> MaterialTheme.colors.background.copy(alpha = 0f)
        DayState.FUTURE, DayState.ACTIVE -> MaterialTheme.colors.background
        DayState.LUCID -> MaterialTheme.colors.primary
    }
    val alpha = when (state) {
        DayState.ACTIVE, DayState.LUCID -> 1f
        DayState.FUTURE -> 0.4f
        DayState.INACTIVE -> 0f
    }
    CompositionLocalProvider(LocalContentAlpha provides alpha) {
        Surface(
            color = color,
            elevation = elevation,
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
            contentColor = MaterialTheme.colors.onBackground,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text, style = MaterialTheme.typography.h6)
            }
        }
    }
}

enum class DayState {
    INACTIVE,
    FUTURE,
    ACTIVE,
    LUCID
}