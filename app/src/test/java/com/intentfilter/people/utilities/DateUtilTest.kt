package com.intentfilter.people.utilities

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate

internal class DateUtilTest {
    @Test
    internal fun shouldFormatDate() {
        val localDate = LocalDate.of(2019, 5, 5)

        val formattedDate = DateUtil.format(localDate)

        assertThat(formattedDate, `is`("05 May, 2019"))
    }
}