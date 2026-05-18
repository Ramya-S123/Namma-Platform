package com.namma.platform.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(value: String) = _uiState.update { it.copy(email = value, errorMessage = null) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value, errorMessage = null) }
    fun updateConfirmPassword(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    fun updateFullName(value: String) = _uiState.update { it.copy(fullName = value, errorMessage = null) }
    fun clearMessages() = _uiState.update { it.copy(errorMessage = null, successMessage = null) }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        val validationError = validateLogin(state.email, state.password)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.login(state.email.trim(), state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Login failed")
                    }
                }
        }
    }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        val validationError = validateRegister(state)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            authRepository.register(
                state.fullName.trim(),
                state.email.trim(),
                state.password
            )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Registration failed")
                    }
                }
        }
    }

    fun resetPassword() {
        val email = _uiState.value.email.trim()
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Enter a valid email for password reset") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.resetPassword(email)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Password reset email sent!"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Reset failed")
                    }
                }
        }
    }

    private fun validateLogin(email: String, password: String): String? {
        if (email.isBlank()) return "Email is required"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email format"
        if (password.length < 6) return "Password must be at least 6 characters"
        return null
    }

    private fun validateRegister(state: AuthUiState): String? {
        if (state.fullName.isBlank()) return "Full name is required"
        if (state.email.isBlank()) return "Email is required"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) return "Invalid email format"
        if (state.password.length < 6) return "Password must be at least 6 characters"
        if (state.password != state.confirmPassword) return "Passwords do not match"
        return null
    }
}
