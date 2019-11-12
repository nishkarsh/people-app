package com.intentfilter.people.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import javax.inject.Inject

class EditProfileFragment : Fragment(), ViewModelStoreOwner {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private lateinit var unbinder: Unbinder
    private val logger = Logger.loggerFor(EditProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, parent, false).apply {
            ButterKnife.bind(this@EditProfileFragment, this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.create().inject(this)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        viewModel.choiceAttributes.observe(viewLifecycleOwner, Observer {
            logger.d("Got choice attributes from service, initializing inputs")
        })
    }

    @OnClick(R.id.view_birthday)
    fun displayDatePicker() {
        DatePickerDialogFragment.newInstance().show(childFragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}