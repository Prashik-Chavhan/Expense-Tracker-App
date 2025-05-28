package com.example.expensetracker.core.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.Date

object DateUtil {

    @SuppressLint("ConstantLocale")
    private  val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private fun parseDate(date: String): Date? = try {
        formatter.parse(date)
    } catch (_: Exception) {
        null
    }

    fun previousDay(date: String): String {
        return parseDate(date)?.let { date ->
            Calendar.getInstance().apply {
                time = date
                add(Calendar.DATE, -1)
            }.time.let(formatter::format)
        } ?: ""
    }

    fun generate7daysPriorDate(date: String): String {
        return parseDate(date)?.let { date ->
            Calendar.getInstance().apply {
                time = date
                add(Calendar.DATE, -6)
            }.time.let(formatter::format)
        } ?: ""
    }

    fun datesBetween(startDate: String, endDate: String): List<String> {
        val start = parseDate(startDate)
        val end = parseDate(endDate)

        if (start == null || end == null) return emptyList()

        val dates = mutableListOf<String>()
        val calendar =     Calendar.getInstance().apply { time = start }

        while(!calendar.time.after(end)) {
            dates.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun generateFormatDate(date: LocalDate): String {
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthValue.toString().padStart(2, '0')

        return "$day/$month/${date.year}"
    }

    fun getWeekDates(dateString: String): List<String> {
        return parseDate(dateString)?.let { date ->
            val calendar =     Calendar.getInstance().apply {
                time = date
                set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            }
            List(7) {
                formatter.format(calendar.time).also { calendar.add(Calendar.DATE, 1) }
            }
        } ?: emptyList()
    }


    fun getMonthDates(dateString: String): List<String> {
        return parseDate(dateString)?.let { date ->
            val calender =     Calendar.getInstance().apply {
                time = date
                set(Calendar.DAY_OF_MONTH, 1)
            }
            val daysInMonth = calender.getActualMaximum(Calendar.DAY_OF_MONTH)
            List(daysInMonth) {
                formatter.format(calender.time).also { calender.add(Calendar.DATE, 1) }
            }
        } ?: emptyList()
    }

    fun getActualDayOfWeek(dateString: String): String {
        return parseDate(dateString)?.let { date ->
            val calendar =     Calendar.getInstance().apply { time = date }
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                Calendar.SUNDAY -> "Sunday"
                else -> ""
            }
        } ?: ""
    }
}