package com.intentfilter.people.views.common.datepicker

import com.intentfilter.people.extensions.InstantExecutorExtension
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.threeten.bp.LocalDate

@Extensions(
    ExtendWith(MockitoExtension::class),
    ExtendWith(RandomBeansExtension::class),
    ExtendWith(InstantExecutorExtension::class)
)
internal class DatePickerViewModelTest {
    @InjectMocks
    lateinit var viewModel: DatePickerViewModel

    @Test
    internal fun shouldSetDateConsideringStartingOfMonthFromZero() {
        viewModel.setDate(2019, 11, 7)

        assertThat(viewModel.selectedDate.value, `is`(LocalDate.of(2019, 12, 7)))
    }
}