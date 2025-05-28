package com.example.expensetracker.core.util

import android.content.Context
import android.widget.Toast
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}

fun localDateTimeToDate(localDate: LocalDate, localTime: LocalTime): Date {
    val localDateTime = LocalDateTime.of(localDate, localTime)
    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}

fun getNumericInitialValue(value: Int): String {
    return if (value == 0) {
        ""
    } else {
        value.toString()
    }
}

fun convertTimeMillisToLocalDate(timeMillis: Long): LocalDate {
    // Create an Instant from the timeMillis value
    val instant = Instant.ofEpochMilli(timeMillis)

    // Specify the desired time zone if needed (default is your system's time zone)
    val zoneId = ZoneId.systemDefault() // Or any specific ZoneId like ZoneId.of("UTC")

    // Convert the Instant to a LocalDate in the specified time zone
    return instant.atZone(zoneId).toLocalDate()
}

fun getFormattedTime(selectedHour: Int, selectedMinute: Int): LocalTime {
    // Create a LocalTime from the selected hour and minute
    return LocalTime.of(selectedHour, selectedMinute)
}

fun truncate(str: String, n: Int): String {
    if (str.length > n) {
        return str.substring(0, n - 1) + "...."
    } else {
        return str
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}