package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Profile(
    @JsonProperty("id") val id: String?,
    @JsonProperty("displayName") val displayName: String,
    @JsonProperty("actualFullName") val actualFullName: String,
    @JsonProperty("profilePicturePath") var profilePicturePath: String?,
    @JsonProperty("birthday") val birthday: String,
    @JsonProperty("genderId") val genderId: String,
    @JsonProperty("ethnicityId") var ethnicityId: String?,
    @JsonProperty("religionId") var religionId: String?,
    @JsonProperty("height") val height: Double?,
    @JsonProperty("figureTypeId") var figureTypeId: String?,
    @JsonProperty("maritalStatusId") val maritalStatusId: String,
    @JsonProperty("occupation") var occupation: String?,
    @JsonProperty("aboutMe") var aboutMe: String?,
    @JsonProperty("location") val location: Coordinates,
    @JsonProperty("version") val version: Long?
)

data class Coordinates(
    @JsonProperty("latitude") val latitude: String,
    @JsonProperty("longitude") val longitude: String
)
