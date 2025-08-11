package com.yawara.tracking.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.data.repository.UserRepository
import com.yawara.tracking.domain.model.CheckIn
import com.yawara.tracking.domain.model.Post
import com.yawara.tracking.domain.model.User
import com.yawara.tracking.domain.usecase.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class DashboardViewModel(private val userRepository: UserRepository) : ViewModel() {


    // var chartDatesStateFlow by mutableStateOf<List<String>>(emptyList())
    private val _chartDatesStateFlow = MutableStateFlow<Set<String>>(emptySet())
    val chartDatesStateFlow: MutableStateFlow<Set<String>> = _chartDatesStateFlow

    var userData by mutableStateOf<User?>(null)
        private set


    init {
        viewModelScope.launch {
            userData = userRepository.getUserInfo()
            //fetchThirtyCheckInsByUser()
        }

    }

    private suspend fun getUserInfo(uid: String) {
        try {
            Log.i("UID INFO 1", "$uid")
            val snapshot = FirebaseManager.firestore.collection("users").document(uid).get().await()
            userData = snapshot.toObject(User::class.java)?.copy(uid = uid)
            //userData?.uid = uid
            Log.i("UID INFO 2", "${userData?.uid}")
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error getting user info", e)
        }
    }

    suspend fun fetchThirtyCheckInsByUser() {
            val thirtyDaysAgo = Utils.getLastThirtyDaysZeroed()

            Log.i("UID INFO", "${FirebaseManager.auth.currentUser?.uid}")
            Log.i("UID INFO USER", "${userData?.uid}")
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
}