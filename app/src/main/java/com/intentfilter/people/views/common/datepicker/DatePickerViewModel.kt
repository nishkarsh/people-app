package com.intentfilter.people.views.common.datepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate

class DatePickerViewModel : ViewModel() {
    val selectedDate = MutableLiveData<LocalDate>()

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.value = LocalDate.of(year, month, dayOfMonth)
    }
}