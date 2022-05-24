package com.matthias.dreamz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class SettingsDataStoreManager(@ApplicationContext val context: Context) {

    private val jwtTokenKey = stringPreferencesKey("jwt_token")

    private val onlineModeKey = booleanPreferencesKey("online_mode")

    suspend fun writeJwtToken(jwtToken: String) {
        context.authDataStore.edit {
            it[jwtTokenKey] = jwtToken
        }
    }

    suspend fun removeJwtToken() {
        context.authDataStore.edit {
            it.remove(jwtTokenKey)
        }
    }

    fun getJwtToken(): Flow<String?> {
        return context.authDataStore.data.map { it[jwtTokenKey] }
    }

    suspend fun writeOnlineMode(offlineMode: Boolean) {
        context.authDataStore.edit { it[onlineModeKey] = offlineMode }
    }

    fun getOnlineMode(): Flow<Boolean?> {
        return  context.authDataStore.data.map { it[onlineModeKey] }
    }
}