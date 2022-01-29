package com.matthias.dreamz.widget

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import com.matthias.dreamz.repository.DreamRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DreamzWidgetReceiver : GlanceAppWidgetReceiver() {
    private val coroutine = MainScope();
    override val glanceAppWidget: DreamzWidget
        get() = DreamzWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        coroutine.launch(Dispatchers.IO) {
            context?.updateWidgets()

        }
    }

    companion object {
        suspend fun Context.updateWidgets() {
            val entryPoint =
                EntryPointAccessors.fromApplication(this, DreamzWidgetEntryPoint::class.java)
            val dreamRepository = entryPoint.dreamRepository()
            val nbDreamInWeek = dreamRepository.getNbDreamWeek()
            val dreams = dreamRepository.getDreamOfWeek();
            val manager = GlanceAppWidgetManager(this)
            manager.getGlanceIds(DreamzWidget::class.java).forEach { id ->
                DreamzWidget().updateAppWidgetState<Preferences>(
                    context = this,
                    glanceId = id
                ) { preferences ->
                    preferences.toMutablePreferences().apply {
                        this[DreamzWidget.countPreferenceKey] = nbDreamInWeek
                        this[DreamzWidget.datesPreferenceKey] =
                            dreams.map { it.date.toString() }.toSet()
                    }
                }
                DreamzWidget().update(this, id)
            }

        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DreamzWidgetEntryPoint {
        fun dreamRepository(): DreamRepository
    }
}