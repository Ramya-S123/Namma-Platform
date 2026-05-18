package com.namma.platform.domain.repository

import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun getAllStations(): Flow<List<Station>>
    fun getStationWithTrains(stationId: Long): Flow<Station?>
    fun searchStations(query: String): Flow<List<Station>>
    fun getFavoriteStations(): Flow<List<Station>>
    fun getTrainsForStation(stationId: Long): Flow<List<Train>>
    fun searchTrains(stationId: Long, query: String): Flow<List<Train>>
    fun getTrainById(trainId: Long): Flow<Train?>
    suspend fun toggleFavorite(stationId: Long, isFavorite: Boolean)
    suspend fun addRecentSearch(stationName: String)
    fun getRecentSearches(): Flow<List<String>>
}
