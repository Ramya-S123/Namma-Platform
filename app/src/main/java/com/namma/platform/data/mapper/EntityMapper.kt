package com.namma.platform.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.namma.platform.data.local.entity.StationEntity
import com.namma.platform.data.local.entity.TrainEntity
import com.namma.platform.data.local.entity.UserEntity
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.model.TrainStatus
import com.namma.platform.domain.model.UserProfile

object EntityMapper {

    private val gson = Gson()
    private val coachListType = object : TypeToken<List<String>>() {}.type

    fun StationEntity.toDomain(trains: List<Train> = emptyList()): Station =
        Station(
            id = id,
            name = name,
            nameKannada = nameKannada,
            code = code,
            isFavorite = isFavorite,
            trains = trains
        )

    fun TrainEntity.toDomain(): Train =
        Train(
            id = id,
            stationId = stationId,
            trainNumber = trainNumber,
            trainName = trainName,
            trainNameKannada = trainNameKannada,
            platform = platform,
            arrivalTime = arrivalTime,
            departureTime = departureTime,
            status = TrainStatus.fromString(status),
            coachSequence = gson.fromJson(coachSequenceJson, coachListType),
            generalCoachPosition = generalCoachPosition,
            ladiesCoachPosition = ladiesCoachPosition
        )

    fun UserEntity.toDomain(): UserProfile =
        UserProfile(uid = uid, fullName = fullName, email = email)

    fun UserProfile.toEntity(): UserEntity =
        UserEntity(uid = uid, fullName = fullName, email = email)
}
