package com.namma.platform.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.namma.platform.data.local.NammaDatabase
import com.namma.platform.data.mapper.EntityMapper.toDomain
import com.namma.platform.data.mapper.EntityMapper.toEntity
import com.namma.platform.domain.model.UserProfile
import com.namma.platform.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: NammaDatabase
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun authStateFlow(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        trySend(firebaseAuth.currentUser != null)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun login(email: String, password: String): Result<UserProfile> =
        runCatching {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalStateException("Login failed")
            getOrCreateProfile(user)
        }

    override suspend fun register(fullName: String, email: String, password: String): Result<UserProfile> =
        runCatching {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalStateException("Registration failed")
            user.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(fullName).build()
            ).await()
            val profile = UserProfile(uid = user.uid, fullName = fullName, email = email)
            database.userDao().insertUser(profile.toEntity())
            profile
        }

    override suspend fun resetPassword(email: String): Result<Unit> =
        runCatching {
            firebaseAuth.sendPasswordResetEmail(email).await()
        }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun getUserProfile(uid: String): Flow<UserProfile?> =
        database.userDao().getUserById(uid).map { it?.toDomain() }

    private suspend fun getOrCreateProfile(user: FirebaseUser): UserProfile {
        val profile = UserProfile(
            uid = user.uid,
            fullName = user.displayName ?: user.email?.substringBefore("@") ?: "User",
            email = user.email ?: ""
        )
        database.userDao().insertUser(profile.toEntity())
        return profile
    }
}
