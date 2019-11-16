package com.intentfilter.people.views.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.intentfilter.people.models.Locations
import com.intentfilter.people.models.NamedAttribute
import com.intentfilter.people.models.Profile
import com.intentfilter.people.models.SingleChoiceAttributes
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
}