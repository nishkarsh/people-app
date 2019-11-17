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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val attributeService: AttributeService, private val locationService: LocationService,
    private val profileService: ProfileService, private val preferences: Preferences,
    private val profileAdapter: ViewableProfileAdapter, private val networkCoroutineDispatcher: CoroutineDispatcher = IO
) : ViewModel() {

    val error = MediatorLiveData<Exception>()
    val locations = MutableLiveData<Locations>()
    val choiceAttributes = MutableLiveData<SingleChoiceAttributes>()
    val viewableProfile: MediatorLiveData<ViewableProfile> = MediatorLiveData()
    val profile: MutableLiveData<Profile?> = MutableLiveData()

    private val logger = Logger.loggerFor(ProfileViewModel::class)

    init {
        trySync()
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

    fun saveProfile(): RestResponse<Unit> {
        logger.d("Saving updated profile: ${viewableProfile.value}")

        val restResponse = RestResponse<Unit>()
        viewModelScope.launch(networkCoroutineDispatcher) {
            try {
                fromViewableProfile(viewableProfile.value, locations.value, choiceAttributes.value, profile.value)?.let {
                    profileService.updateProfile(it)
                }
                restResponse.success.postValue(Unit)
            } catch (exception: Exception) {
                logger.e("An error occurred while saving profile: $exception")
                restResponse.error.postValue(exception)
            }
        }

        return restResponse
    }

    fun getGenderOptions(): Array<NamedAttribute> {
        return choiceAttributes.value!!.gender
    }

    fun getEthnicityOptions(): Array<NamedAttribute> {
        return choiceAttributes.value!!.ethnicity
    }

    fun getFigureTypeOptions(): Array<NamedAttribute> {
        return choiceAttributes.value!!.figure
    }

    fun getReligionOptions(): Array<NamedAttribute> {
        return choiceAttributes.value!!.religion
    }

    fun getMaritalStatusOptions(): Array<NamedAttribute> {
        return choiceAttributes.value!!.maritalStatus
    }

    fun setProfilePicture(imageUri: Uri?) {
        viewableProfile.let {
            val current = it.value
            current?.profilePicturePath = imageUri?.toString()
            it.postValue(current)
        }
    }

    private fun toViewableProfile(profile: Profile?, locations: Locations?, attrs: SingleChoiceAttributes?): ViewableProfile? {
        return if (profile == null || locations == null || attrs == null) null
        else profileAdapter.from(profile, locations, attrs)
    }

    private fun fromViewableProfile(
        viewableProfile: ViewableProfile?, locations: Locations?, attributes: SingleChoiceAttributes?, currentProfile: Profile?
    ): Profile? {
        return if (viewableProfile == null || locations == null || attributes == null || currentProfile == null) null
        else profileAdapter.from(viewableProfile, locations, attributes, currentProfile)
    }
}
