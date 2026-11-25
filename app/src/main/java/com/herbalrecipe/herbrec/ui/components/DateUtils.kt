package com.herbalrecipe.herbrec.ui.components

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateString(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return "21 November 2026"
    return try {
        val inputFormat = if (rawDate.contains("T")) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        }
        val date: Date? = inputFormat.parse(rawDate)
        if (date != null) {
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
            outputFormat.format(date)
        } else {
            rawDate
        }
    } catch (_: Exception) {
        rawDate
    }
}

fun formatDateTimeString(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return "23 November 2026, 22:36"
    return try {
        val inputFormat = if (rawDate.contains("T")) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        }
        val date: Date? = inputFormat.parse(rawDate)
        if (date != null) {
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
            outputFormat.format(date)
        } else {
            rawDate
        }
    } catch (_: Exception) {
        rawDate
    }
}
