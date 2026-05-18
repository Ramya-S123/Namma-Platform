package com.namma.platform.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.repository.AuthRepository
import com.namma.platform.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val isLoggedIn: Boolean = false,
    val isDarkMode: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        authRepository.authStateFlow(),
        preferencesManager.isDarkMode
    ) { loggedIn, darkMode ->
        MainUiState(isLoggedIn = loggedIn, isDarkMode = darkMode)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )
}
