package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Profile(
    @JsonProperty("id") val id: String,
    @JsonProperty("displayName") val displayName: String,
    @JsonProperty("actualFullName") val actualFullName: String,
    @JsonProperty("profilePicturePath") val profilePicturePath: String?,
    @JsonProperty("birthday") val birthday: String,
    @JsonProperty("genderId") val genderId: String,
    @JsonProperty("ethnicityId") val ethnicityId: String?,
    @JsonProperty("religionId") val religionId: String?,
    @JsonProperty("height") val height: Double?,
    @JsonProperty("figureTypeId") val figureTypeId: String?,
    @JsonProperty("maritalStatusId") val maritalStatusId: String,
    @JsonProperty("occupation") val occupation: String?,
    @JsonProperty("aboutMe") val aboutMe: String?,
    @JsonProperty("location") val location: Coordinates,
    @JsonProperty("version") val version: Long
)

data class Coordinates(
    @JsonProperty("latitude") val latitude: String,
    @JsonProperty("longitude") val longitude: String
)
