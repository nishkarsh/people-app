package com.intentfilter.people.models

data class ViewableProfile(
    val displayName: String,
    val actualFullName: String,
    val profilePicturePath: String?,
    val birthday: String,
    val gender: String,
    val ethnicity: String?,
    val religion: String?,
    val height: String?,
    val figureType: String?,
    val maritalStatus: String,
    val occupation: String?,
    val aboutMe: String?,
    val location: String
)