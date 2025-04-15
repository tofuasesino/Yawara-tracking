package com.yawara.tracking.domain.model

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val title: String = "",
    val type: String = "",
    val content: String = "",
    val author: String = "",
    val videoUrl: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
