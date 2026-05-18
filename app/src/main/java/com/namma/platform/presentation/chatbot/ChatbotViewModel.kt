package com.namma.platform.presentation.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.utils.ChatbotHelper
import com.namma.platform.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(val text: String, val isUser: Boolean)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(ChatMessage("ನಮಸ್ಕಾರ! ನಾನು ನಿಮ್ಮ ರೈಲು ಸಹಾಯಕ. ಹೇಗೆ ಸಹಾಯ ಮಾಡಲಿ?", false))
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input.asStateFlow()

    fun updateInput(value: String) { _input.value = value }

    fun sendMessage() {
        val query = _input.value.trim()
        if (query.isBlank()) return
        viewModelScope.launch {
            val useKannada = preferencesManager.isKannada.first()
            _messages.value = _messages.value + ChatMessage(query, true)
            _input.value = ""
            val response = ChatbotHelper.getResponse(query, useKannada)
            _messages.value = _messages.value + ChatMessage(response, false)
        }
    }
}
