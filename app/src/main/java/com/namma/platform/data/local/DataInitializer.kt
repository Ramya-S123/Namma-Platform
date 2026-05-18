package com.namma.platform.data.local

import android.content.Context
import com.google.gson.Gson
import com.namma.platform.data.local.entity.StationEntity
import com.namma.platform.data.local.entity.TrainEntity
import com.namma.platform.data.remote.dto.StationsDataDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: NammaDatabase,
    private val gson: Gson
) {

    suspend fun initializeIfNeeded() {
        val stationCount = database.stationDao().getStationCount()
        if (stationCount > 0) return

        val json = context.assets.open("stations_data.json")
            .bufferedReader()
            .use { it.readText() }

        val data = gson.fromJson(json, StationsDataDto::class.java)

        val stations = data.stations.map { station ->
            StationEntity(
                id = station.id,
                name = station.name,
                nameKannada = station.nameKannada,
                code = station.code
            )
        }

        val trains = data.stations.flatMap { station ->
            station.trains.map { train ->
                TrainEntity(
                    stationId = station.id,
                    trainNumber = train.trainNumber,
                    trainName = train.trainName,
                    trainNameKannada = train.trainNameKannada,
                    platform = train.platform,
                    arrivalTime = train.arrivalTime,
                    departureTime = train.departureTime,
                    status = train.status,
                    coachSequenceJson = gson.toJson(train.coachSequence),
                    generalCoachPosition = train.generalCoachPosition,
                    ladiesCoachPosition = train.ladiesCoachPosition
                )
            }
        }

        database.stationDao().insertStations(stations)
        database.trainDao().insertTrains(trains)
    }
}
