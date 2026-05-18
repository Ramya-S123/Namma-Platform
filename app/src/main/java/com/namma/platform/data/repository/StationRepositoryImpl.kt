package com.namma.platform.data.repository

import com.namma.platform.data.local.NammaDatabase
import com.namma.platform.data.local.entity.RecentSearchEntity
import com.namma.platform.data.mapper.EntityMapper.toDomain
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.repository.StationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepositoryImpl @Inject constructor(
    private val database: NammaDatabase
) : StationRepository {

    override fun getAllStations(): Flow<List<Station>> =
        database.stationDao().getAllStations().map { stations ->
            stations.map { it.toDomain() }
        }

    override fun getStationWithTrains(stationId: Long): Flow<Station?> {
        val stationFlow = database.stationDao().getStationById(stationId)
        val trainsFlow = database.trainDao().getTrainsByStation(stationId)

        return combine(stationFlow, trainsFlow) { station, trains ->
            station?.toDomain(trains = trains.map { it.toDomain() })
        }
    }

    override fun searchStations(query: String): Flow<List<Station>> =
        database.stationDao().searchStations(query).map { list ->
            list.map { it.toDomain() }
        }

    override fun getFavoriteStations(): Flow<List<Station>> =
        database.stationDao().getFavoriteStations().map { list ->
            list.map { it.toDomain() }
        }

    override fun getTrainsForStation(stationId: Long): Flow<List<Train>> =
        database.trainDao().getTrainsByStation(stationId).map { trains ->
            trains.map { it.toDomain() }
        }

    override fun searchTrains(stationId: Long, query: String): Flow<List<Train>> =
        database.trainDao().searchTrains(stationId, query).map { trains ->
            trains.map { it.toDomain() }
        }

    override fun getTrainById(trainId: Long): Flow<Train?> =
        database.trainDao().getTrainById(trainId).map { it?.toDomain() }

    override suspend fun toggleFavorite(stationId: Long, isFavorite: Boolean) {
        database.stationDao().setFavorite(stationId, isFavorite)
    }

    override suspend fun addRecentSearch(stationName: String) {
        database.recentSearchDao().deleteByName(stationName)
        database.recentSearchDao().insertSearch(RecentSearchEntity(stationName = stationName))
    }

    override fun getRecentSearches(): Flow<List<String>> =
        database.recentSearchDao().getRecentSearches().map { list ->
            list.map { it.stationName }
        }
}
