package com.intentfilter.people.views.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.intentfilter.people.models.NamedAttribute
import com.intentfilter.people.models.SingleChoiceAttributes
import com.intentfilter.people.services.AttributeService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val service: AttributeService,
    networkCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val choiceAttributes: LiveData<SingleChoiceAttributes> = liveData(networkCoroutineDispatcher) {
        emit(service.getAttributes())
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
}