package com.namma.platform.presentation.train

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.repository.StationRepository
import com.namma.platform.utils.PreferencesManager
import com.namma.platform.utils.TtsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrainDetailUiState(
    val train: Train? = null,
    val isKannada: Boolean = true,
    val isLoading: Boolean = true
)

@HiltViewModel
class TrainDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stationRepository: StationRepository,
    private val preferencesManager: PreferencesManager,
    private val ttsManager: TtsManager
) : ViewModel() {

    private val trainId: Long = savedStateHandle.get<Long>("trainId") ?: 0L

    private val _uiState = MutableStateFlow(TrainDetailUiState())
    val uiState: StateFlow<TrainDetailUiState> = _uiState.asStateFlow()

    init {
        ttsManager.initialize()
        viewModelScope.launch {
            combine(
                stationRepository.getTrainById(trainId),
                preferencesManager.isKannada
            ) { train, isKannada ->
                TrainDetailUiState(train = train, isKannada = isKannada, isLoading = train == null)
            }.collect { _uiState.value = it }
        }
    }

    fun speakAnnouncement() {
        val train = _uiState.value.train ?: return
        val useKannada = _uiState.value.isKannada
        val text = ttsManager.buildAnnouncement(
            trainNameKannada = if (useKannada) train.trainNameKannada else train.trainName,
            platform = train.platform,
            generalPosition = train.generalCoachPosition,
            useKannada = useKannada
        )
        ttsManager.speakKannada(text)
    }
}
