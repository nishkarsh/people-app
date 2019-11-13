package com.intentfilter.people.views.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.intentfilter.people.DaggerPeopleComponent
import com.intentfilter.people.R
import com.intentfilter.people.extensions.observeOnce
import com.intentfilter.people.models.NamedAttribute
import com.intentfilter.people.utilities.DateUtil
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.views.common.CircleTransform
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment.Companion.TAG
import com.intentfilter.people.views.common.datepicker.DatePickerViewModel
import com.intentfilter.people.views.common.itemchooser.SingleAttributeChooserFragment
import com.intentfilter.people.views.common.itemchooser.SingleAttributeChooserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import javax.inject.Inject

class EditProfileFragment : Fragment(), ViewModelStoreOwner {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private lateinit var unbinder: Unbinder
    private val logger = Logger.loggerFor(EditProfileFragment::class)

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, parent, false)
        unbinder = ButterKnife.bind(this, view)

        attachBirthdayChooserObserver()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.create().inject(this)

        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        profileViewModel.choiceAttributes.observe(viewLifecycleOwner, Observer {
            logger.d("Got choice attributes from service, initializing inputs")
        })
    }

    @OnClick(R.id.profilePicture)
    fun displayFileChooser() {
        val chooseImageIntent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        startActivityForResult(chooseImageIntent, 0)
    }

    @OnClick(R.id.viewBirthday)
    fun displayDatePicker() {
        DatePickerDialogFragment.newInstance().show(childFragmentManager, TAG)
    }

    @OnClick(R.id.viewGender)
    fun displayGenderOptions(view: EditText) {
        displayChooser(view, profileViewModel.getGenderOptions(), R.string.title_gender)
    }

    @OnClick(R.id.viewEthnicity)
    fun displayEthnicityOptions(view: EditText) {
        displayChooser(view, profileViewModel.getEthnicityOptions(), R.string.title_ethnicity)
    }

    @OnClick(R.id.viewFigureType)
    fun displayFigureOptions(view: EditText) {
        displayChooser(view, profileViewModel.getFigureTypeOptions(), R.string.title_figure_type)
    }

    @OnClick(R.id.viewReligion)
    fun displayReligionOptions(view: EditText) {
        displayChooser(view, profileViewModel.getReligionOptions(), R.string.title_figure_type)
    }

    @OnClick(R.id.viewMaritalStatus)
    fun displayMaritalStatusOptions(view: EditText) {
        displayChooser(view, profileViewModel.getMaritalStatusOptions(), R.string.title_marital_status)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.d(String.format("Received activity result for request code: %s", requestCode))

        profileViewModel.setProfilePicture(data?.data)
        data?.let {
            Picasso.with(context).load(data.data)
                .transform(CircleTransform())
                .into(profilePicture)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    private fun displayChooser(view: EditText, options: Array<NamedAttribute>, @StringRes titleId: Int) {
        // TODO Handle configuration change while reusing the same code
        val chooserFragment = SingleAttributeChooserFragment.newInstance(options, getString(titleId))
        chooserFragment.showNow(childFragmentManager, TAG)

        val chooserViewModel = ViewModelProvider(chooserFragment).get(SingleAttributeChooserViewModel::class.java)
        chooserViewModel.selectedAttribute.observeOnce(viewLifecycleOwner, Observer {
            view.setText(it.name)
        })
    }

    private fun attachBirthdayChooserObserver() {
        val birthdayChooserViewModel = ViewModelProvider(this).get(DatePickerViewModel::class.java)
        birthdayChooserViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            viewBirthday.setText(DateUtil.format(it))
        })
    }
}