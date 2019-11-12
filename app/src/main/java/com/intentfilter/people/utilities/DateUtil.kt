package com.intentfilter.people.utilities

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class DateUtil {
    companion object {
        const val FORMAT = "dd MMMM, yyyy"

        @JvmStatic
        fun format(date: LocalDate): String {
            return date.format(DateTimeFormatter.ofPattern(FORMAT))
        }
    }
}