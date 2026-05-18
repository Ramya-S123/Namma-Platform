package com.namma.platform.domain.model

enum class TrainStatus {
    ON_TIME,
    DELAYED,
    CANCELLED;

    companion object {
        fun fromString(value: String): TrainStatus =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: ON_TIME
    }
}
