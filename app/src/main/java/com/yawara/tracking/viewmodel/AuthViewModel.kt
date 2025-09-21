package com.yawara.tracking.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.data.repository.UserRepository
import com.yawara.tracking.data.model.CheckIn
import com.yawara.tracking.data.model.Post
import com.yawara.tracking.data.model.User
import com.yawara.tracking.domain.usecase.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {


    private var _chartDatesStateFlow = MutableStateFlow<Set<String>>(emptySet())
    val chartDatesStateFlow: MutableStateFlow<Set<String>> = _chartDatesStateFlow

    var userData by mutableStateOf<User?>(null)
        private set


    init {
        viewModelScope.launch {
            userData = userRepository.getUserInfo()
        }

    }

    private suspend fun getUserInfo(uid: String) {
        try {
            val snapshot = FirebaseManager.firestore.collection("users").document(uid).get().await()
            userData = snapshot.toObject(User::class.java)?.copy(uid = uid)
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error getting user info", e)
        }
    }

    suspend fun fetchThirtyCheckInsByUser() {
        val thirtyDaysAgo = Utils.getLastThirtyDaysZeroed()

        val snapshot =
            FirebaseManager.firestore.collection("checkIns")
                .whereEqualTo("userId", userData?.uid)
                .whereGreaterThanOrEqualTo("timestamp", thirtyDaysAgo)
                .get()
                .await()

        val checkIns = snapshot.documents.mapNotNull { it.toObject(CheckIn::class.java) }

        val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
        val checkInDates = checkIns.map { dateFormat.format(it.timestamp.toDate()) }.toSet()

        _chartDatesStateFlow.value = checkInDates
    }

    fun createPost(title: String, content: String, videoUrl: String, author: String) {

        val type: String = if (videoUrl.isEmpty()) {
            "post"
        } else {
            "video"
        }

        val post = Post(
            title = title,
            content = content,
            type = type,
            videoUrl = videoUrl,
            author = author
        )
        FirebaseManager.firestore.collection("posts").add(post)
    }

    fun closeSession() {
        FirebaseManager.auth.signOut()

    }
}