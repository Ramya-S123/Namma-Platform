package com.namma.platform.domain.model

data class Train(
    val id: Long = 0,
    val stationId: Long,
    val trainNumber: String,
    val trainName: String,
    val trainNameKannada: String,
    val platform: Int,
    val arrivalTime: String,
    val departureTime: String,
    val status: TrainStatus,
    val coachSequence: List<String>,
    val generalCoachPosition: Int,
    val ladiesCoachPosition: Int
)
