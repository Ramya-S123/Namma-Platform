package com.namma.platform.domain.model

data class Station(
    val id: Long,
    val name: String,
    val nameKannada: String,
    val code: String,
    val isFavorite: Boolean = false,
    val trains: List<Train> = emptyList()
)
