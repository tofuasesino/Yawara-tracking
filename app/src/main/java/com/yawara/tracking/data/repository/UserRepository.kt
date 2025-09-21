package com.yawara.tracking.data.repository

import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    suspend fun getUserInfo(): User? {
        val userId = FirebaseManager.auth.currentUser?.uid ?: return null
        val snapshot = FirebaseManager.firestore.collection("users").document(userId).get().await()
        return snapshot.toObject(User::class.java)?.copy(uid = snapshot.id)
    }
}