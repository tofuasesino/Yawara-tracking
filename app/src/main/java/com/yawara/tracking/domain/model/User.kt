package com.yawara.tracking.domain.model

import com.google.firebase.Timestamp
import java.util.Date

data class User(
    var uid: String = "",
    val name: String = "",
    val role: String = "",
    val email: String = "",
    val joinDate: Timestamp? = null,
    val lastRankUpdate: Timestamp? = null,
    val rank: String = ""
)
