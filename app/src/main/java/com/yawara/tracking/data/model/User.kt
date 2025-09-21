package com.yawara.tracking.data.model

import com.google.firebase.Timestamp

data class User(
    var uid: String = "",
    val name: String = "",
    val role: String = "",
    val email: String = "",
    val joinDate: Timestamp? = null,
    val lastRankUpdate: Timestamp? = null,
    val rank: String = ""
)
