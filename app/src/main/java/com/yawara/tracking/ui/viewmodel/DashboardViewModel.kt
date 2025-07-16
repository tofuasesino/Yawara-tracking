package com.yawara.tracking.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.CheckIn
import com.yawara.tracking.domain.usecase.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class DashboardViewModel : ViewModel() {

    private val _chartDatesStateFlow = MutableStateFlow<Set<String>>(emptySet())
    val chartDatesStateFlow: MutableStateFlow<Set<String>> = _chartDatesStateFlow

    init {
        fetchThirtyCheckInsByUser()
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
}