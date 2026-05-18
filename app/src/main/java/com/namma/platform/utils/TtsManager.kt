package com.namma.platform.utils

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TtsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    fun initialize(onReady: (Boolean) -> Unit = {}) {
        if (tts != null) {
            onReady(isReady)
            return
        }
        tts = TextToSpeech(context) { status ->
            isReady = status == TextToSpeech.SUCCESS
            if (isReady) {
                val kannadaLocale = Locale.forLanguageTag("kn-IN")
                val result = tts?.setLanguage(kannadaLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale("kn")
                }
                tts?.setSpeechRate(0.85f)
                tts?.setPitch(1.0f)
            }
            onReady(isReady)
        }
    }

    fun speakKannada(text: String, onDone: () -> Unit = {}) {
        if (!isReady) {
            initialize { ready ->
                if (ready) speakInternal(text, onDone)
            }
            return
        }
        speakInternal(text, onDone)
    }

    private fun speakInternal(text: String, onDone: () -> Unit) {
        val utteranceId = "namma_announcement"
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { onDone() }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) { onDone() }
            override fun onError(utteranceId: String?, errorCode: Int) { onDone() }
        })
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun buildAnnouncement(
        trainNameKannada: String,
        platform: Int,
        generalPosition: Int,
        useKannada: Boolean = true
    ): String {
        return if (useKannada) {
            "$trainNameKannada ಪ್ಲಾಟ್‌ಫಾರ್ಮ್ $platform ಕ್ಕೆ ಬರುತ್ತಿದೆ. " +
                "ಸಾಮಾನ್ಯ ಬೋಗಿ ಎಂಜಿನ್ ನಂತರ ${generalPosition}ನೇ ಸ್ಥಾನದಲ್ಲಿದೆ. " +
                "ದಯವಿಟ್ಟು ಸುರಕ್ಷಿತವಾಗಿ ನಿಲ್ಲಿ."
        } else {
            "$trainNameKannada is arriving at Platform $platform. " +
                "General coach is at position ${generalPosition + 1} after the engine. " +
                "Please stand safely."
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}
