package com.intentfilter.people.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intentfilter.people.adapters.ViewableProfileAdapter
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
import com.intentfilter.people.services.ProfileService
import com.intentfilter.people.utilities.Preferences
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val attributeService: AttributeService, private val locationService: LocationService,
    private val profileService: ProfileService, private val preferences: Preferences,
    private val viewableProfileAdapter: ViewableProfileAdapter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                attributeService, locationService, profileService, preferences, viewableProfileAdapter
            ) as T
        } else throw IllegalArgumentException()
    }
}