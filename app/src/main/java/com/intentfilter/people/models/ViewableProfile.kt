package com.intentfilter.people.models

data class ViewableProfile(
    var displayName: String,
    var actualFullName: String,
    var profilePicturePath: String?,
    var birthday: String,
    var gender: String,
    var ethnicity: String?,
    var religion: String?,
    var height: String?,
    var figureType: String?,
    var maritalStatus: String,
    var occupation: String?,
    var aboutMe: String?,
    var location: String
)