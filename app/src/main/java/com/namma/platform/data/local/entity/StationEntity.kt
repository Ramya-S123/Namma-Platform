package com.namma.platform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val nameKannada: String,
    val code: String,
    val isFavorite: Boolean = false
)
