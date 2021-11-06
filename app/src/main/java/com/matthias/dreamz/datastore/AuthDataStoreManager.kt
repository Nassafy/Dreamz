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

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStoreManager(@ApplicationContext val context: Context) {

    private val jwtTokenKey = stringPreferencesKey("jwt_token")

    suspend fun writeJwtToken(jwtToken: String) {
        context.settingsDataStore.edit { settings ->
            settings[jwtTokenKey] = jwtToken
        }
    }

    fun getJwtToken(): Flow<String?> {
        return context.settingsDataStore.data.map { it[jwtTokenKey] }
    }
}