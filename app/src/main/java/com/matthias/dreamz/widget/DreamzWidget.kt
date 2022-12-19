package com.matthias.dreamz.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.matthias.dreamz.R
import com.matthias.dreamz.repository.DreamRepository
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.widget.DreamzWidgetReceiver.Companion.updateWidgets
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate


class DreamzWidget() : GlanceAppWidget() {
    companion object {
        val countPreferenceKey = intPreferencesKey("count-key")
        val datesPreferenceKey = stringSetPreferencesKey("dates-key")

    }

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val count = prefs[countPreferenceKey] ?: 0
        val dates = prefs[datesPreferenceKey] ?: setOf()

        Box(
            modifier = GlanceModifier.cornerRadius(8.dp).background(Color.DarkGray).clickable(
                onClick = actionRunCallback<UpdateActionCallback>()
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically,
                modifier = GlanceModifier.fillMaxSize()
            ) {
                Text(
                    text = LocalContext.current.resources.getString(R.string.dreamz_widget_text),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(Color.White)
                    ),
                )
                Spacer(modifier = GlanceModifier.height(20.dp))
                Text(
                    text = "$count / 7",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = ColorProvider(Color.White),
                        fontWeight = FontWeight.Bold
                    ),
                )
                Spacer(modifier = GlanceModifier.height(20.dp))
                WeekBar(dates)
            }
        }
    }

    @Composable
    fun WeekBar(dates: Set<String>) {
        val now = LocalDate.now()
        val currentDay = now.dayOfWeek.value - 1
        val daysOfWeek = LocalContext.current.resources.getStringArray(R.array.week_days)
        val days = daysOfWeek.takeLast(6 - currentDay) + daysOfWeek.dropLast(6 - currentDay)
        Row() {
            days.forEachIndexed { index, day ->
                val dayDate = now.minusDays(6 - index.toLong()).toString()
                val style = if (dates.contains(dayDate)) TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color(0xFFEF6C00)),
                    fontSize = 12.sp
                ) else TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(Color(0xFFFFFFFF))
                )
                Text(text = day, modifier = GlanceModifier.padding(2.dp), style = style)
            }
        }
    }

    fun loadData() {
        actionRunCallback<UpdateActionCallback>()
    }
}

class UpdateActionCallback : ActionCallback {
    private val coroutine = MainScope()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DreamzWidgetEntryPoint {
        fun dreamRepository(): DreamRepository
    }

    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        coroutine.launch(Dispatchers.IO) {
            context.updateWidgets()
        }
    }
}

class TodayActionCallback : ActionCallback {
    private val coroutine = MainScope()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DreamzWidgetEntryPoint {
        fun dreamRepository(): DreamRepository
    }

    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val entryPoint =
            EntryPointAccessors.fromApplication(
                context,
                DreamzWidgetEntryPoint::class.java
            )
        val dreamRepository = entryPoint.dreamRepository()
        coroutine.launch(Dispatchers.IO) {
            val todayDream = dreamRepository.getTodayDreamDay().first()
            val dreamId = todayDream?.uid ?: 0
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse(Screen.EditDream.createDeepLink(dreamId))
            context.startActivity(intent)
        }
    }
}
