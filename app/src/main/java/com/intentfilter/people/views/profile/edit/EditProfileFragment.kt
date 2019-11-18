package com.intentfilter.people.views.profile.edit

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.PermissionManager.PermissionRequestListener
import com.intentfilter.androidpermissions.models.DeniedPermissions
import com.intentfilter.people.DaggerPeopleComponent
import com.intentfilter.people.R
import com.intentfilter.people.databinding.FragmentEditProfileBinding
import com.intentfilter.people.extensions.observeOnce
import com.intentfilter.people.models.NamedAttribute
import com.intentfilter.people.utilities.DateUtil
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.utilities.TempImageFileBuilder
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment
import com.intentfilter.people.views.common.datepicker.DatePickerDialogFragment.Companion.TAG
import com.intentfilter.people.views.common.datepicker.DatePickerViewModel
import com.intentfilter.people.views.common.itemchooser.SingleAttributeChooserFragment
import com.intentfilter.people.views.common.itemchooser.SingleAttributeChooserViewModel
import com.intentfilter.people.views.profile.ProfileViewModel
import com.intentfilter.people.views.profile.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileFragment : Fragment(), ViewModelStoreOwner {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    private lateinit var unbinder: Unbinder
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentEditProfileBinding

    private val logger = Logger.loggerFor(EditProfileFragment::class)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, parent, false)
        unbinder = ButterKnife.bind(this, binding.root)

        attachBirthdayChooserObserver()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerPeopleComponent.factory().newInstance(requireContext()).inject(this)

        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java).apply {
            binding.isEditMode = isEditMode()

            locations.observe(viewLifecycleOwner, Observer {
                logger.d("Got locations from service, initializing autocomplete")

                viewLocation.setAdapter(LocationsAdapter(requireContext(), it.cities))
            })

            viewableProfile.observe(viewLifecycleOwner, Observer {
                logger.d("Got viewable profile from service, initializing inputs")
                binding.profile = it
            })
        }
    }

    @OnClick(R.id.buttonSave)
    fun saveProfile() {
        val errors = profileViewModel.validateProfile()
        binding.errors = errors

        if (errors.none()) {
            profileViewModel.saveProfile().apply {
                success.observe(viewLifecycleOwner,
                    Observer { findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment) })
                error.observe(viewLifecycleOwner,
                    Observer { Snackbar.make(requireView(), R.string.profile_update_error, LENGTH_LONG).show() })
            }
        }
    }

    @OnClick(R.id.profilePicture)
    fun askPermissionsAndChooseFile() {
        PermissionManager.getInstance(requireContext())
            .checkPermissions(arrayListOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), object : PermissionRequestListener {
                override fun onPermissionGranted() {
                    displayImageChooser()
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions?) {
                    notifyPermissionNeeded()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logger.d("Received activity result for request code: $requestCode")

        profileViewModel.viewModelScope.launch(Dispatchers.Default) {
            data?.data?.let { uri ->
                val imageFile = TempImageFileBuilder.createFile(requireContext(), uri)
                profileViewModel.setProfilePicture(uri, imageFile)
            }
        }
    }

    @OnClick(value = [R.id.viewBirthday, R.id.viewBirthdayWrapper])
    fun displayDatePicker() {
        DatePickerDialogFragment.newInstance().show(childFragmentManager, TAG)
    }

    @OnClick(value = [R.id.viewGender, R.id.viewGenderWrapper])
    fun displayGenderOptions() {
        displayOptionChooser(viewGender, profileViewModel.getGenderOptions(), R.string.title_gender)
    }

    @OnClick(value = [R.id.viewEthnicity, R.id.viewEthnicityWrapper])
    fun displayEthnicityOptions() {
        displayOptionChooser(viewEthnicity, profileViewModel.getEthnicityOptions(), R.string.title_ethnicity)
    }

    @OnClick(value = [R.id.viewFigureType, R.id.viewFigureTypeWrapper])
    fun displayFigureOptions() {
        displayOptionChooser(viewFigureType, profileViewModel.getFigureTypeOptions(), R.string.title_figure_type)
    }

    @OnClick(value = [R.id.viewReligion, R.id.viewReligionWrapper])
    fun displayReligionOptions() {
        displayOptionChooser(viewReligion, profileViewModel.getReligionOptions(), R.string.title_figure_type)
    }

    @OnClick(value = [R.id.viewMaritalStatus, R.id.viewMaritalStatusWrapper])
    fun displayMaritalStatusOptions() {
        displayOptionChooser(viewMaritalStatus, profileViewModel.getMaritalStatusOptions(), R.string.title_marital_status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    private fun displayOptionChooser(view: EditText, options: Array<NamedAttribute>?, @StringRes titleId: Int) {
        if (options.isNullOrEmpty()) {
            Snackbar.make(requireView(), R.string.not_yet_fetched, LENGTH_LONG).show()
            return
        }

        // TODO Handle configuration change while reusing the same code
        val chooserFragment = SingleAttributeChooserFragment.newInstance(options, getString(titleId))
        chooserFragment.showNow(childFragmentManager, TAG)

        val chooserViewModel = ViewModelProvider(chooserFragment).get(SingleAttributeChooserViewModel::class.java)
        chooserViewModel.selectedAttribute.observeOnce(viewLifecycleOwner, Observer {
            view.setText(it.name)
        })
    }

    private fun displayImageChooser() {
        val chooseImageIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        startActivityForResult(chooseImageIntent, 0)
    }

    private fun attachBirthdayChooserObserver() {
        val birthdayChooserViewModel = ViewModelProvider(this).get(DatePickerViewModel::class.java)
        birthdayChooserViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            viewBirthday.setText(DateUtil.format(it))
        })
    }

    private fun notifyPermissionNeeded() {
        val actionListener: (View) -> Unit = {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", requireContext().packageName, null)
            })
        }

        Snackbar.make(requireView(), R.string.message_storage_permission, LENGTH_LONG)
            .setAction(R.string.label_settings, actionListener).show()
    }
}