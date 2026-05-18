package com.namma.platform.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.namma.platform.data.local.NammaDatabase
import com.namma.platform.data.repository.AuthRepositoryImpl
import com.namma.platform.data.repository.StationRepositoryImpl
import com.namma.platform.domain.repository.AuthRepository
import com.namma.platform.domain.repository.StationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NammaDatabase =
        Room.databaseBuilder(
            context,
            NammaDatabase::class.java,
            "namma_platform_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideStationRepository(impl: StationRepositoryImpl): StationRepository = impl

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}
