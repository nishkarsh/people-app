package com.intentfilter.people.adapters

import com.intentfilter.people.BuildConfig
import com.intentfilter.people.models.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

open class ViewableProfileAdapter @Inject constructor() {

    open fun from(profile: Profile, locations: Locations, choiceAttributes: SingleChoiceAttributes): ViewableProfile {
        return ViewableProfile(
            profile.displayName, profile.actualFullName, completeImageUri(profile), formatBirthday(profile.birthday),
            getAttributeName(profile.genderId, choiceAttributes.gender)!!,
            getAttributeName(profile.ethnicityId, choiceAttributes.ethnicity),
            getAttributeName(profile.religionId, choiceAttributes.religion), profile.height?.toString(),
            getAttributeName(profile.figureTypeId, choiceAttributes.figure),
            getAttributeName(profile.maritalStatusId, choiceAttributes.maritalStatus)!!,
            profile.occupation, profile.aboutMe, getCityName(locations, profile.location)!!
        )
    }

    open fun from(viewable: ViewableProfile, locations: Locations, attrs: SingleChoiceAttributes, current: Profile?): Profile? {
        return Profile(
            current?.id, viewable.displayName!!, viewable.actualFullName!!, stripPrefix(viewable.profilePicturePath),
            parseBirthday(viewable.birthday!!).toString(), getAttributeId(viewable.gender, attrs.gender).toString(),
            getAttributeId(viewable.ethnicity, attrs.ethnicity).toString(),
            getAttributeId(viewable.religion, attrs.religion).toString(), viewable.height?.toDouble(),
            getAttributeId(viewable.figureType, attrs.figure).toString(),
            getAttributeId(viewable.maritalStatus, attrs.maritalStatus).toString(),
            viewable.occupation, viewable.aboutMe, getCityCoordinates(locations, viewable.location!!), current?.version
        )
    }

    private fun completeImageUri(profile: Profile): String? {
        return profile.profilePicturePath?.let { BuildConfig.IMAGE_UPLOAD_PATH + profile.profilePicturePath }
    }

    private fun stripPrefix(profilePicturePath: String?): String? {
        return profilePicturePath?.let { profilePicturePath.removePrefix(BuildConfig.IMAGE_UPLOAD_PATH) }
    }

    private fun getAttributeId(attributeName: String?, attributes: Array<NamedAttribute>): UUID? {
        return attributes.find { attribute -> attribute.name == attributeName }?.id
    }

    private fun getAttributeName(attributeId: String?, attributes: Array<NamedAttribute>): String? {
        return attributes.find { attribute -> attribute.id.toString() == attributeId }?.name
    }

    private fun getCityName(locations: Locations, coordinates: Coordinates) =
        locations.cities.find { city -> city.latitude == coordinates.latitude && city.longitude == coordinates.longitude }?.name

    private fun getCityCoordinates(locations: Locations, cityName: String): Coordinates {
        val city = locations.cities.find { city -> (city.name == cityName) }
        return Coordinates(city!!.latitude, city.longitude)
    }

    private fun parseBirthday(birthday: String): LocalDate {
        return LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd MMMM, yyyy"))
    }

    private fun formatBirthday(birthday: String): String {
        return LocalDate.parse(birthday).format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"))
    }
}