package com.namma.platform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.namma.platform.data.local.entity.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {

    @Query("SELECT * FROM stations ORDER BY name ASC")
    fun getAllStations(): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE id = :stationId")
    fun getStationById(stationId: Long): Flow<StationEntity?>

    @Query("SELECT * FROM stations WHERE name LIKE '%' || :query || '%' OR nameKannada LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%'")
    fun searchStations(query: String): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteStations(): Flow<List<StationEntity>>

    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getStationCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Update
    suspend fun updateStation(station: StationEntity)

    @Query("UPDATE stations SET isFavorite = :isFavorite WHERE id = :stationId")
    suspend fun setFavorite(stationId: Long, isFavorite: Boolean)
}
