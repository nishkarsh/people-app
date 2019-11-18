package com.intentfilter.people.views.common.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.util.*

class DatePickerDialogFragment : AppCompatDialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: DatePickerViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        viewModel = ViewModelProvider(parentFragment as ViewModelStoreOwner).get(DatePickerViewModel::class.java)

        return DatePickerDialog(context!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.setDate(year, month, dayOfMonth)
    }

    companion object {
        val TAG = DatePickerDialogFragment::class.java.canonicalName

        @JvmStatic
        fun newInstance(): DatePickerDialogFragment {
            return DatePickerDialogFragment()
        }
    }
}