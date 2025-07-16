package com.yawara.tracking.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.yawara.tracking.data.datasource.FirebaseManager

class ProfileScreenViewModel : ViewModel() {
    fun closeSession() {
        FirebaseManager.auth.signOut()

    }
}