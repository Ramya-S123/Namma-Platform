package com.namma.platform.domain.repository

import com.namma.platform.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Any?
    fun isLoggedIn(): Boolean
    fun authStateFlow(): Flow<Boolean>
    suspend fun login(email: String, password: String): Result<UserProfile>
    suspend fun register(fullName: String, email: String, password: String): Result<UserProfile>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout()
    fun getUserProfile(uid: String): Flow<UserProfile?>
}
