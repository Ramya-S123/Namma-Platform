package com.namma.platform.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isKannada: Boolean = true,
    val notificationsEnabled: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesManager.isDarkMode,
        preferencesManager.isKannada,
        preferencesManager.notificationsEnabled
    ) { dark, kannada, notifications ->
        SettingsUiState(
            isDarkMode = dark,
            isKannada = kannada,
            notificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setDarkMode(enabled) }
    }

    fun setKannada(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setKannada(enabled) }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setNotifications(enabled) }
    }
}
