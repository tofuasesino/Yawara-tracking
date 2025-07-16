package com.yawara.tracking.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.CheckIn
import com.yawara.tracking.domain.usecase.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class CheckInViewModel : ViewModel() {
    private val db = FirebaseManager.firestore.collection("checkIns")

    private val _alreadyCheckIn = mutableStateOf<Boolean?>(null)
    val alreadyCheckedIn: State<Boolean?> = _alreadyCheckIn

    var itemList by mutableStateOf<List<CheckIn>>(emptyList())

    init {
        checkLastCheckIn()
    }

    fun fetchCheckIns() {
        db
            .get()
            .addOnSuccessListener { docs ->
                Log.d("Firestore results", "Cogiendo datos")
                itemList = docs.toObjects(CheckIn::class.java)
                Log.d("Firestore results", "Datos recogidos.")
            }
            .addOnFailureListener { exception ->
                Log.i("Firestore results", "Error: ", exception)
            }
    }


    fun checkInUser() {

        if (_alreadyCheckIn.value == true) return

        viewModelScope.launch {
            val checkIn = hashMapOf(
                "userId" to FirebaseManager.auth.currentUser?.uid.toString(),
                "timestamp" to Utils.getDateZeroed()
            )

            db.document().set(checkIn)
                .addOnSuccessListener {
                    Log.i("CheckIn", "Satisfactorio")
                    _alreadyCheckIn.value = true

                }
                .addOnFailureListener { Log.i("CheckIn", ":(") }
        }
    }


    // Falta implementar la comprobación de si el usuario ya ha realizado el checkIn hoy.
    private fun checkLastCheckIn() {
        viewModelScope.launch {
            val snapshot = db
                .whereEqualTo("userId", FirebaseManager.auth.currentUser?.uid.toString())
                .whereEqualTo("timestamp", Utils.getDateZeroed())
                .get()
                .await()

            _alreadyCheckIn.value = !snapshot.isEmpty
        }
    }



}