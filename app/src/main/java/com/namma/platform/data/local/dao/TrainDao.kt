package com.namma.platform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namma.platform.data.local.entity.TrainEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainDao {

    @Query("SELECT * FROM trains WHERE stationId = :stationId ORDER BY arrivalTime ASC")
    fun getTrainsByStation(stationId: Long): Flow<List<TrainEntity>>

    @Query("SELECT * FROM trains WHERE id = :trainId")
    fun getTrainById(trainId: Long): Flow<TrainEntity?>

    @Query("SELECT * FROM trains WHERE stationId = :stationId AND (trainName LIKE '%' || :query || '%' OR trainNumber LIKE '%' || :query || '%') ORDER BY arrivalTime ASC")
    fun searchTrains(stationId: Long, query: String): Flow<List<TrainEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrains(trains: List<TrainEntity>)

    @Query("DELETE FROM trains")
    suspend fun deleteAllTrains()
}
