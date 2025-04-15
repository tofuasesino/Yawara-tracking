package com.yawara.tracking.domain.usecase

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun parseDate(createdAt: Timestamp): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val time = createdAt.toDate()
        return dateFormat.format(time)
    }

    fun extractVideoId(videoUrl: String): String? {
        // You can extract the video ID from a YouTube URL
        // Example: https://www.youtube.com/watch?v=videoId
        val regex = """(?:https?://)?(?:www\.)?youtube\.com/watch\?v=([a-zA-Z0-9_-]+)""".toRegex()
        return regex.find(videoUrl)?.groupValues?.get(1)
    }
}