package com.namma.platform.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("namma_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        val KEY_KANNADA = booleanPreferencesKey("kannada_language")
        val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications")
        val KEY_SELECTED_STATION = longPreferencesKey("selected_station_id")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { it[KEY_DARK_MODE] ?: false }
    val isKannada: Flow<Boolean> = dataStore.data.map { it[KEY_KANNADA] ?: true }
    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_NOTIFICATIONS] ?: true }
    val selectedStationId: Flow<Long> = dataStore.data.map { it[KEY_SELECTED_STATION] ?: 1L }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[KEY_DARK_MODE] = enabled }
    }

    suspend fun setKannada(enabled: Boolean) {
        dataStore.edit { it[KEY_KANNADA] = enabled }
    }

    suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { it[KEY_NOTIFICATIONS] = enabled }
    }

    suspend fun setSelectedStation(stationId: Long) {
        dataStore.edit { it[KEY_SELECTED_STATION] = stationId }
    }
}
