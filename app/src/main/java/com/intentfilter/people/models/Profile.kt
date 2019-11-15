package com.intentfilter.people.models

import android.net.Uri

data class Profile(
    var id: String,
    var displayName: String,
    var actualFullName: String,
    var profilePicturePath: Uri,
    var birthday: String,
    var genderId: String,
    var ethnicityId: String,
    var religionId: String,
    var height: Double,
    var figureTypeId: String,
    var maritalStatusId: String,
    var occupation: String,
    var aboutMe: String,
    var location: City,
    var version: Long
)
