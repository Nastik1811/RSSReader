package com.example.rssr.utils


import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        fun getFormattedDate(sourceDateString: String): String {
            val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val date:Date = sourceSdf.parse(sourceDateString)?:Date()

            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return  sdf.format(date)
        }
    }
}