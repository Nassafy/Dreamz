package com.matthias.dreamz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.filtersDataStore: DataStore<Preferences> by preferencesDataStore(name = "filter")

class FilterDataStoreManager(@ApplicationContext val context: Context) {
    private val textKey = stringPreferencesKey("text")
    private val tagKey = stringPreferencesKey("tag")
    private val peopleKey = stringPreferencesKey("people")
    private val minNoteKey = intPreferencesKey("minNote")
    private val maxNoteKey = intPreferencesKey("maxNote")

    val text: Flow<String?> = context.filtersDataStore.data.map { it[textKey] }

    suspend fun setText(text: String) {
        context.filtersDataStore.edit { filter ->
            filter[textKey] = text
        }
    }

    val tag: Flow<String?> = context.filtersDataStore.data.map { it[tagKey] }

    suspend fun setTag(tag: String) {
        context.filtersDataStore.edit { filter ->
            filter[tagKey] = tag
        }
    }

    val people: Flow<String?> = context.filtersDataStore.data.map { it[peopleKey] }

    suspend fun setPeople(people: String) {
        context.filtersDataStore.edit { filter ->
            filter[peopleKey] = people
        }
    }

    val minNote: Flow<Int?> = context.filtersDataStore.data.map { it[minNoteKey] }

    suspend fun setMinNote(minNote: Int?) {
        context.filtersDataStore.edit { filter ->
            if (minNote != null) {
                filter[minNoteKey] = minNote
            } else {
                filter.remove(minNoteKey)
            }
        }
    }

    val maxNote: Flow<Int?> = context.filtersDataStore.data.map { it[maxNoteKey] }

    suspend fun setMaxNote(maxNote: Int?) {
        context.filtersDataStore.edit { filter ->
            if (maxNote != null) {
                filter[maxNoteKey] = maxNote
            } else {
                filter.remove(maxNoteKey)
            }
        }
    }

}