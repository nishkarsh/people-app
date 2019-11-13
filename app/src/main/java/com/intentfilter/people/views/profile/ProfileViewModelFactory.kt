package com.intentfilter.people.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val attributeService: AttributeService, private var locationService: LocationService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(attributeService, locationService) as T
        } else throw IllegalArgumentException()
    }
}