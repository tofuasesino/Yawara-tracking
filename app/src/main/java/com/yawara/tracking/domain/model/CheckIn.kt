package com.yawara.tracking.domain.model

import com.google.firebase.Timestamp

data class CheckIn(
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
