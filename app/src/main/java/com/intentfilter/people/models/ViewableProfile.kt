package com.intentfilter.people.models

data class ViewableProfile(
    var displayName: String? = null,
    var actualFullName: String? = null,
    var profilePicturePath: String? = null,
    var birthday: String? = null,
    var gender: String? = null,
    var ethnicity: String? = null,
    var religion: String? = null,
    var height: String? = null,
    var figureType: String? = null,
    var maritalStatus: String? = null,
    var occupation: String? = null,
    var aboutMe: String? = null,
    var location: String? = null
)