package com.yawara.tracking.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawara.tracking.data.datasource.FirebaseManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {


    fun getCurrentUser() {
        viewModelScope.launch {
            val user = FirebaseManager.auth.currentUser
            if (user != null) {
                fetchUserRole(user.uid)
            }
        }
    }

    fun fetchUserRole(userId: String) {
        viewModelScope.launch {
            val user = FirebaseManager.firestore.collection("users").document(userId).get().await()

        }
    }
}