package com.matthias.dreamz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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

}