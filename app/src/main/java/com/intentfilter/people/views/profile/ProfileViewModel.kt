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

    val profilePicture = MutableLiveData<Uri>()
    val error = MutableLiveData<String>()
    val locations = MutableLiveData<Locations>()
    val choiceAttributes = MutableLiveData<SingleChoiceAttributes>()
    val profile: MutableLiveData<Profile?> = MutableLiveData()
    val viewableProfile: MediatorLiveData<ViewableProfile> = MediatorLiveData()

    private val logger = Logger.loggerFor(ProfileViewModel::class)

    init {
        triggerFetch()

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

    fun triggerFetch() {
        logger.d("Triggering fetch of profile, locations and choice attributes")

        viewModelScope.launch(networkCoroutineDispatcher) {
            try {
                locations.postValue(locationService.getLocations())
                choiceAttributes.postValue(attributeService.getAttributes())

                preferences.getProfile()?.let { profileId ->
                    preferences.saveProfile(profileId)
                    profile.postValue(profileService.getProfile(profileId))
                }
            } catch (exception: Exception) {
                logger.e("An error occurred while fetching data: $exception")
                error.postValue(exception.message)
            }
        }
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
        imageUri?.let { profilePicture.postValue(imageUri) }
    }

    private fun toViewableProfile(profile: Profile?, locations: Locations?, attrs: SingleChoiceAttributes?): ViewableProfile? {
        return if (profile == null || locations == null || attrs == null) null
        else profileAdapter.from(profile, locations, attrs)
    }
}
