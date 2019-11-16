package com.intentfilter.people.views.profile

import android.net.Uri
import androidx.lifecycle.*
import com.intentfilter.people.adapters.ViewableProfileAdapter
import com.intentfilter.people.models.*
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
import com.intentfilter.people.services.ProfileService
import com.intentfilter.people.utilities.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val attributeService: AttributeService, private val locationService: LocationService,
    private val profileService: ProfileService, private val preferences: Preferences,
    networkCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var profilePicture = MutableLiveData<Uri>()

    val choiceAttributes: LiveData<SingleChoiceAttributes> = liveData(networkCoroutineDispatcher) {
        emit(attributeService.getAttributes())
    }

    val locations: LiveData<Locations> = liveData(networkCoroutineDispatcher) {
        emit(locationService.getLocations())
    }

    val profile: LiveData<Profile?> = liveData(networkCoroutineDispatcher) {
        preferences.getProfile()?.let { profileId ->
            preferences.saveProfile(profileId)
            emit(profileService.getProfile(profileId))
        }
    }

    var viewableProfile: MediatorLiveData<ViewableProfile> = MediatorLiveData()

    init {
        viewableProfile.addSource(choiceAttributes) { attributes ->
            toViewableProfile(profile.value, locations.value, attributes)?.let { viewableProfile.postValue(it) }
        }
        viewableProfile.addSource(locations) { locations ->
            toViewableProfile(profile.value, locations, choiceAttributes.value)?.let { viewableProfile.postValue(it) }
        }
        viewableProfile.addSource(profile) { profile ->
            toViewableProfile(profile, locations.value, choiceAttributes.value)?.let { viewableProfile.postValue(it) }
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

    private fun toViewableProfile(
        profile: Profile?, locations: Locations?, attributes: SingleChoiceAttributes?
    ): ViewableProfile? {
        return if (profile == null || locations == null || attributes == null) null
        else ViewableProfileAdapter.convert(profile, locations, attributes)
    }
}
