package com.namma.platform.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StationsDataDto(
    @SerializedName("stations") val stations: List<StationJsonDto>
)

data class StationJsonDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("nameKannada") val nameKannada: String,
    @SerializedName("code") val code: String,
    @SerializedName("trains") val trains: List<TrainJsonDto>
)

data class TrainJsonDto(
    @SerializedName("trainNumber") val trainNumber: String,
    @SerializedName("trainName") val trainName: String,
    @SerializedName("trainNameKannada") val trainNameKannada: String,
    @SerializedName("platform") val platform: Int,
    @SerializedName("arrivalTime") val arrivalTime: String,
    @SerializedName("departureTime") val departureTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("coachSequence") val coachSequence: List<String>,
    @SerializedName("generalCoachPosition") val generalCoachPosition: Int,
    @SerializedName("ladiesCoachPosition") val ladiesCoachPosition: Int
)
