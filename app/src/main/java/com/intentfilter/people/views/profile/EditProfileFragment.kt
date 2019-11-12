package com.intentfilter.people.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.intentfilter.people.DaggerPeopleComponent
import com.intentfilter.people.R
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment.Companion.TAG
import com.intentfilter.people.views.common.datepicker.DatePickerViewModel
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class EditProfileFragment : Fragment(), ViewModelStoreOwner {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private lateinit var unbinder: Unbinder
    private val logger = Logger.loggerFor(EditProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, parent, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.create().inject(this)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.choiceAttributes.observe(viewLifecycleOwner, Observer {
            logger.d("Got choice attributes from service, initializing inputs")
        })
    }

    @OnClick(R.id.viewBirthday)
    fun displayDatePicker(view: EditText) {
        val viewModel = ViewModelProvider(this).get(DatePickerViewModel::class.java)

        viewModel.selectedDate.observe(this, Observer {
            view.setText(it.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy")))
        })

        DatePickerDialogFragment.newInstance().show(childFragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}