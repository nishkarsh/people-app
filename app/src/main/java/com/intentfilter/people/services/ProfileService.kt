package com.intentfilter.people.services

import com.intentfilter.people.gateways.ProfileServiceGateway
import com.intentfilter.people.models.Profile
import okhttp3.MediaType.parse
import okhttp3.MultipartBody
import okhttp3.RequestBody.create
import java.io.File
import javax.inject.Inject

open class ProfileService @Inject constructor(private val gateway: ProfileServiceGateway) {

    open suspend fun getProfile(id: String): Profile? {
        return gateway.get(id)
    }

    open suspend fun createProfile(profile: Profile): Profile {
        return gateway.create(profile)
    }

    open suspend fun updateProfile(profile: Profile) {
        gateway.update(profile.id, profile)
    }

    open suspend fun uploadPicture(picture: File): String {
        val filePart = MultipartBody.Part.createFormData("file", picture.name, create(parse("image/*"), picture))
        return gateway.uploadPicture(filePart)
    }
}