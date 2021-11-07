package com.matthias.dreamz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.flagDataStore: DataStore<Preferences> by preferencesDataStore(name = "flag")

class FlagDataStoreManager(@ApplicationContext val context: Context) {
    private val syncStateKey = booleanPreferencesKey("syncState")

    val syncState: Flow<Boolean> = context.flagDataStore.data.map { it[syncStateKey] ?: true }

    suspend fun setSyncState(syncState: Boolean) {
        context.flagDataStore.edit { flags ->
            flags[syncStateKey] = syncState
        }
    }
}