package com.intentfilter.people.validators

import com.intentfilter.people.R
import com.intentfilter.people.models.ViewableProfile
import javax.inject.Inject

open class ProfileFormValidator @Inject constructor() {

    open fun validateFields(profile: ViewableProfile): ValidationResult {
        val validationResult = ValidationResult()

        if (profile.displayName.isNullOrEmpty()) validationResult[R.id.inputDisplayName] = R.string.error_required
        if (profile.actualFullName.isNullOrEmpty()) validationResult[R.id.inputActualFullName] = R.string.error_required
        if (profile.birthday.isNullOrEmpty()) validationResult[R.id.viewBirthdayWrapper] = R.string.error_required
        if (profile.gender.isNullOrEmpty()) validationResult[R.id.viewGenderWrapper] = R.string.error_required
        if (profile.maritalStatus.isNullOrEmpty()) validationResult[R.id.viewMaritalStatusWrapper] = R.string.error_required
        if (profile.location.isNullOrEmpty()) validationResult[R.id.viewLocation] = R.string.error_required

        return validationResult
    }
}