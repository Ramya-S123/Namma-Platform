package com.namma.platform.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.namma.platform.data.local.dao.RecentSearchDao
import com.namma.platform.data.local.dao.StationDao
import com.namma.platform.data.local.dao.TrainDao
import com.namma.platform.data.local.dao.UserDao
import com.namma.platform.data.local.entity.RecentSearchEntity
import com.namma.platform.data.local.entity.StationEntity
import com.namma.platform.data.local.entity.TrainEntity
import com.namma.platform.data.local.entity.UserEntity

@Database(
    entities = [
        StationEntity::class,
        TrainEntity::class,
        UserEntity::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NammaDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun trainDao(): TrainDao
    abstract fun userDao(): UserDao
    abstract fun recentSearchDao(): RecentSearchDao
}
