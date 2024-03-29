package com.intentfilter.people.views.profile

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intentfilter.people.adapters.ViewableProfileAdapter
import com.intentfilter.people.models.*
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
import com.intentfilter.people.services.ProfileService
import com.intentfilter.people.utilities.Logger
import com.intentfilter.people.utilities.Preferences
import com.intentfilter.people.validators.ProfileFormValidator
import com.intentfilter.people.validators.ValidationResult
import com.intentfilter.people.views.profile.edit.Mode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val attributeService: AttributeService, private val locationService: LocationService,
    private val profileService: ProfileService, private val preferences: Preferences, private val validator: ProfileFormValidator,
    private val profileAdapter: ViewableProfileAdapter, private val networkCoroutineDispatcher: CoroutineDispatcher = IO
) : ViewModel() {

    val error = MediatorLiveData<Exception>()
    val locations = MutableLiveData<Locations>()
    val choiceAttributes = MutableLiveData<SingleChoiceAttributes>()
    val viewableProfile: MediatorLiveData<ViewableProfile> = MediatorLiveData()
    val profile: MutableLiveData<Profile?> = MutableLiveData()

    private lateinit var mode: Mode
    internal var selectedProfilePicture: File? = null
    private val logger = Logger.loggerFor(ProfileViewModel::class)

    init {
        trySync()

        initializeMode()

        viewableProfile.apply {
            addSource(choiceAttributes) { attributes ->
                toViewableProfile(profile.value, locations.value, attributes)?.let { viewableProfile.postValue(it) }
            }
            addSource(locations) { locations ->
                toViewableProfile(profile.value, locations, choiceAttributes.value)?.let { viewableProfile.postValue(it) }
            }
            addSource(profile) { profile ->
                toViewableProfile(profile, locations.value, choiceAttributes.value)?.let { viewableProfile.postValue(it) }
            }
        }
    }

    fun initiateSync(): RestResponse<Unit> {
        logger.d("Triggering fetch of profile, locations and choice attributes")

        val restResponse = RestResponse<Unit>()
        viewModelScope.launch(networkCoroutineDispatcher) {
            try {
                locations.postValue(locationService.getLocations())
                choiceAttributes.postValue(attributeService.getAttributes())

                preferences.getProfile()?.let { profileId ->
                    preferences.saveProfile(profileId)
                    profile.postValue(profileService.getProfile(profileId))
                }
                restResponse.success.postValue(Unit)
            } catch (exception: Exception) {
                logger.e("An error occurred while fetching data: $exception")
                restResponse.error.postValue(exception)
            }
        }

        return restResponse
    }

    fun trySync() {
        val syncResponse = initiateSync()
        error.addSource(syncResponse.error) { error.postValue(it) }
    }

    fun validateProfile(): ValidationResult {
        return validator.validateFields(viewableProfile.value!!)
    }

    fun saveProfile(): RestResponse<Unit> {
        logger.d("Saving profile: ${viewableProfile.value}")

        val restResponse = RestResponse<Unit>()
        viewModelScope.launch(networkCoroutineDispatcher) {
            try {
                fromViewableProfile(viewableProfile.value, locations.value, choiceAttributes.value, profile.value)?.let {
                    selectedProfilePicture?.let { selectedDisplayPicture ->
                        val uploadedPicturePath = profileService.uploadPicture(selectedDisplayPicture)
                        it.profilePicturePath = uploadedPicturePath.fileName
                    }

                    createOrUpdate(it)
                }
                restResponse.success.postValue(Unit)
            } catch (exception: Exception) {
                logger.e("An error occurred while saving profile: $exception")
                restResponse.error.postValue(exception)
            }
        }

        return restResponse
    }

    fun isEditMode(): Boolean = (mode == Mode.Edit)

    fun getGenderOptions(): Array<NamedAttribute>? {
        return choiceAttributes.value?.gender
    }

    fun getEthnicityOptions(): Array<NamedAttribute>? {
        return choiceAttributes.value?.ethnicity
    }

    fun getFigureTypeOptions(): Array<NamedAttribute>? {
        return choiceAttributes.value?.figure
    }

    fun getReligionOptions(): Array<NamedAttribute>? {
        return choiceAttributes.value?.religion
    }

    fun getMaritalStatusOptions(): Array<NamedAttribute>? {
        return choiceAttributes.value?.maritalStatus
    }

    fun setProfilePicture(imageUri: Uri, imageFile: File) {
        selectedProfilePicture = imageFile

        val current = viewableProfile.value ?: ViewableProfile()
        current.profilePicturePath = imageUri.toString()
        viewableProfile.postValue(current)
    }

    private suspend fun createOrUpdate(profile: Profile) {
        if (isEditMode()) {
            profileService.updateProfile(profile)
        } else {
            val createdProfile = profileService.createProfile(profile)
            preferences.saveProfile(createdProfile.id!!)

            logger.d("Created new profile with ID: ${createdProfile.id}")
        }
    }

    private fun toViewableProfile(profile: Profile?, locations: Locations?, attrs: SingleChoiceAttributes?): ViewableProfile? {
        return if (profile == null || locations == null || attrs == null) null
        else profileAdapter.from(profile, locations, attrs)
    }

    private fun fromViewableProfile(
        viewableProfile: ViewableProfile?, locations: Locations?, attributes: SingleChoiceAttributes?, currentProfile: Profile?
    ): Profile? {
        return if (viewableProfile == null || locations == null || attributes == null) null
        else profileAdapter.from(viewableProfile, locations, attributes, currentProfile)
    }

    private fun initializeMode() = when {
        noExistingProfile() -> {
            mode = Mode.Create
            viewableProfile.postValue(ViewableProfile())
        }
        else -> mode = Mode.Edit
    }

    private fun noExistingProfile() = (preferences.getProfile() == null)
}
