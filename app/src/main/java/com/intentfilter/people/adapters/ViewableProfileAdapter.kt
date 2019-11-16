package com.intentfilter.people.adapters

import com.intentfilter.people.models.*

object ViewableProfileAdapter {

    fun convert(profile: Profile, locations: Locations, choiceAttributes: SingleChoiceAttributes): ViewableProfile {
        return ViewableProfile(
            profile.displayName, profile.actualFullName, profile.profilePicturePath, profile.birthday,
            getAttributeName(profile.genderId, choiceAttributes.gender)!!,
            getAttributeName(profile.ethnicityId, choiceAttributes.ethnicity),
            getAttributeName(profile.religionId, choiceAttributes.religion),
            profile.height, getAttributeName(profile.figureTypeId, choiceAttributes.figure),
            getAttributeName(profile.maritalStatusId, choiceAttributes.maritalStatus)!!,
            profile.occupation, profile.aboutMe, getCityName(locations, profile.location)!!
        )
    }

    private fun getAttributeName(attributeId: String, attributes: Array<NamedAttribute>): String? {
        val foundAttribute = attributes.find { attribute -> attribute.id.toString() == attributeId }
        return foundAttribute?.name
    }

    private fun getCityName(locations: Locations, coordinates: Coordinates) =
        locations.cities.find { city -> city.latitude == coordinates.latitude && city.longitude == coordinates.longitude }?.name
}