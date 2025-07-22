package com.yawara.tracking.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.CheckIn
import com.yawara.tracking.domain.model.Post
import com.yawara.tracking.domain.model.User
import com.yawara.tracking.domain.usecase.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class DashboardViewModel : ViewModel() {

    private val _chartDatesStateFlow = MutableStateFlow<Set<String>>(emptySet())
    val chartDatesStateFlow: MutableStateFlow<Set<String>> = _chartDatesStateFlow
    private val _userRole = mutableStateOf<String?>(null)
    val userRole = _userRole

    private val _userName = mutableStateOf<String?>(null)
    val userName = _userName

    var user by mutableStateOf<User?>(null)

    init {
        loadUser()
        fetchThirtyCheckInsByUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            val uid = FirebaseManager.auth.currentUser?.uid
            uid?.let {
                getUserInfo(it)
                _userName.value = user?.name
                _userRole.value = user?.role
            }
        }
    }

    fun fetchUserRole() {

    }

    private suspend fun getUserInfo(uid: String){

        try {
            val snapshot = FirebaseManager.firestore
                .collection("users")
                .document(uid)
                .get()
                .await()

            user = snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.i("UserInfo", "Error getting user info", e)
        }

    }

    fun fetchThirtyCheckInsByUser() {
        viewModelScope.launch {

            val thirtyDaysAgo = Utils.getLastThirtyDaysZeroed()

            val snapshot =
                FirebaseManager.firestore.collection("checkIns")
                    .whereEqualTo("userId", FirebaseManager.auth.currentUser?.uid.toString())
                    .whereGreaterThanOrEqualTo("timestamp", thirtyDaysAgo)
                    .get()
                    .await()

            val checkIns = snapshot.documents.mapNotNull { it.toObject(CheckIn::class.java) }

            val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
            val checkInDates = checkIns.map { dateFormat.format(it.timestamp.toDate()) }.toSet()

            _chartDatesStateFlow.value = checkInDates
        }
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