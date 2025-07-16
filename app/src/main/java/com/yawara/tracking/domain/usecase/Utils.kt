package com.yawara.tracking.domain.usecase

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object Utils {

    // This function returns the current date but with hours, minutes and seconds set to zero,
    // this way, when trying to check the checkins, there is no error.
    fun getDateZeroed(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return Timestamp(calendar.time)
    }

    fun getLastThirtyDaysZeroed(): Timestamp {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -30)
        }

        return Timestamp(calendar.time)
    }

    fun getLastThirtyDaysFormatted(): List<String> {
        val formatter = SimpleDateFormat("dd-MM", Locale.getDefault())
        val calendar = Calendar.getInstance()

        return List(30) {
            val dateStr = formatter.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            dateStr
        }.reversed()
    }

    // This fun is only intended to be used within the Post creation screen, returns date and time the post was created.
    fun parseDate(createdAt: Timestamp): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val time = createdAt.toDate()
        return dateFormat.format(time)
    }

    fun extractVideoId(videoUrl: String): String? {
        // You can extract the video ID from a YouTube URL
        // Example: https://www.youtube.com/watch?v=videoId
        val regex = Regex("""^https?://(?:www\.)?youtube\.com/watch\?v=([A-Za-z0-9_-]{11})(?:[&#?].*)?$""",
        RegexOption.IGNORE_CASE)
        return regex.find(videoUrl)?.groupValues?.get(1)
    }

    fun getDaysOfMonth(): List<String> {
        val locale = Locale("es", "ES")

        val currentDate = LocalDate.now()
        val currentYearMonth = YearMonth.from(currentDate)
        val daysInMonth = currentYearMonth.lengthOfMonth()

        val formatter = DateTimeFormatter.ofPattern("EEE dd MMM", locale)

        var daysList = (1..daysInMonth).map { day ->
            val date = LocalDate.of(currentDate.year, currentDate.month, day)
            date.format(formatter)
        }

        return daysList
    }
}