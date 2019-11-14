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

        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java).apply {
            choiceAttributes.observe(viewLifecycleOwner, Observer {
                logger.d("Got choice attributes from service, initializing inputs")
            })

            locations.observe(viewLifecycleOwner, Observer {
                logger.d("Got locations from service, initializing autocomplete")

                viewLocation.setAdapter(LocationsAdapter(context!!, it.cities))
            })
        }
        attachProfilePictureObserver()
    }

    @OnClick(R.id.profilePicture)
    fun displayFileChooser() {
        val chooseImageIntent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        startActivityForResult(chooseImageIntent, 0)
    }

    @OnClick(value = [R.id.viewBirthday, R.id.viewBirthdayWrapper])
    fun displayDatePicker() {
        DatePickerDialogFragment.newInstance().show(childFragmentManager, TAG)
    }

    @OnClick(value = [R.id.viewGender, R.id.viewGenderWrapper])
    fun displayGenderOptions() {
        displayChooser(viewGender, profileViewModel.getGenderOptions(), R.string.title_gender)
    }

    @OnClick(value = [R.id.viewEthnicity, R.id.viewEthnicityWrapper])
    fun displayEthnicityOptions() {
        displayChooser(viewEthnicity, profileViewModel.getEthnicityOptions(), R.string.title_ethnicity)
    }

    @OnClick(value = [R.id.viewFigureType, R.id.viewFigureTypeWrapper])
    fun displayFigureOptions() {
        displayChooser(viewFigureType, profileViewModel.getFigureTypeOptions(), R.string.title_figure_type)
    }

    @OnClick(value = [R.id.viewReligion, R.id.viewReligionWrapper])
    fun displayReligionOptions() {
        displayChooser(viewReligion, profileViewModel.getReligionOptions(), R.string.title_figure_type)
    }

    @OnClick(value = [R.id.viewMaritalStatus, R.id.viewMaritalStatusWrapper])
    fun displayMaritalStatusOptions() {
        displayChooser(viewMaritalStatus, profileViewModel.getMaritalStatusOptions(), R.string.title_marital_status)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.d(String.format("Received activity result for request code: %s", requestCode))

        profileViewModel.setProfilePicture(data?.data)
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

    private fun attachProfilePictureObserver() {
        profileViewModel.profilePicture.observe(viewLifecycleOwner, Observer {
            Picasso.with(context).load(it).transform(CircleTransform()).into(profilePicture)
        })
    }

    private fun attachBirthdayChooserObserver() {
        val birthdayChooserViewModel = ViewModelProvider(this).get(DatePickerViewModel::class.java)
        birthdayChooserViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            viewBirthday.setText(DateUtil.format(it))
        })
    }
}