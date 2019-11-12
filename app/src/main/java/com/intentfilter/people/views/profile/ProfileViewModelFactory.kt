package com.intentfilter.people.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intentfilter.people.services.AttributeService
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(private val attributeService: AttributeService) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(attributeService) as T
        } else throw IllegalArgumentException()
    }
}