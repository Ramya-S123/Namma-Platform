package com.namma.platform.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.repository.StationRepository
import com.namma.platform.utils.PreferencesManager
import com.namma.platform.utils.TtsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val stations: List<Station> = emptyList(),
    val selectedStationId: Long = 1L,
    val trains: List<Train> = emptyList(),
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val userName: String = "Passenger",
    val isKannada: Boolean = true,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val preferencesManager: PreferencesManager,
    private val ttsManager: TtsManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStationId = MutableStateFlow(1L)
    private val _trains = MutableStateFlow<List<Train>>(emptyList())
    private var trainsJob: Job? = null

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        ttsManager.initialize()
        viewModelScope.launch {
            preferencesManager.selectedStationId.collect { id ->
                _selectedStationId.value = id
                loadTrains(id)
            }
        }
        viewModelScope.launch {
            combine(
                combine(
                    stationRepository.getAllStations(),
                    stationRepository.getRecentSearches(),
                    preferencesManager.isKannada
                ) { stations, recent, isKannada -> Triple(stations, recent, isKannada) },
                combine(_searchQuery, _selectedStationId, _trains) { query, selectedId, trains ->
                    Triple(query, selectedId, trains)
                }
            ) { stationData, trainData ->
                val (stations, recent, isKannada) = stationData
                val (query, selectedId, trains) = trainData
                HomeUiState(
                    stations = stations,
                    selectedStationId = selectedId,
                    trains = trains.take(3),
                    searchQuery = query,
                    recentSearches = recent,
                    userName = firebaseAuth.currentUser?.displayName ?: "Passenger",
                    isKannada = isKannada,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    private fun loadTrains(stationId: Long) {
        trainsJob?.cancel()
        trainsJob = viewModelScope.launch {
            val flow = if (_searchQuery.value.isBlank()) {
                stationRepository.getTrainsForStation(stationId)
            } else {
                stationRepository.searchTrains(stationId, _searchQuery.value)
            }
            flow.collect { _trains.value = it }
        }
    }

    fun selectStation(station: Station) {
        viewModelScope.launch {
            _selectedStationId.value = station.id
            preferencesManager.setSelectedStation(station.id)
            stationRepository.addRecentSearch(station.name)
            loadTrains(station.id)
        }
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
        loadTrains(_selectedStationId.value)
        if (query.isNotBlank()) {
            viewModelScope.launch { stationRepository.addRecentSearch(query) }
        }
    }

    fun toggleFavorite(station: Station) {
        viewModelScope.launch {
            stationRepository.toggleFavorite(station.id, !station.isFavorite)
        }
    }

    fun speakAnnouncement(train: Train) {
        val useKannada = _uiState.value.isKannada
        val text = ttsManager.buildAnnouncement(
            trainNameKannada = if (useKannada) train.trainNameKannada else train.trainName,
            platform = train.platform,
            generalPosition = train.generalCoachPosition,
            useKannada = useKannada
        )
        ttsManager.speakKannada(text)
    }

    fun getFilteredStations(): List<Station> {
        val query = _searchQuery.value
        val all = _uiState.value.stations
        if (query.isBlank()) return all
        return all.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.nameKannada.contains(query) ||
                it.code.contains(query, ignoreCase = true)
        }
    }
}
