package com.namma.platform.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trains",
    foreignKeys = [
        ForeignKey(
            entity = StationEntity::class,
            parentColumns = ["id"],
            childColumns = ["stationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("stationId")]
)
data class TrainEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stationId: Long,
    val trainNumber: String,
    val trainName: String,
    val trainNameKannada: String,
    val platform: Int,
    val arrivalTime: String,
    val departureTime: String,
    val status: String,
    val coachSequenceJson: String,
    val generalCoachPosition: Int,
    val ladiesCoachPosition: Int
)
