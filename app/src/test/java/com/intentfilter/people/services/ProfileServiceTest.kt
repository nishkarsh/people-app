package com.intentfilter.people.services

import com.intentfilter.people.gateways.ProfileServiceGateway
import com.intentfilter.people.models.Profile
import com.nhaarman.mockitokotlin2.argumentCaptor
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.createFormData
import okhttp3.RequestBody.create
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class ProfileServiceTest {
    @Mock
    lateinit var gateway: ProfileServiceGateway

    @InjectMocks
    lateinit var service: ProfileService

    private val fileCaptor = argumentCaptor<MultipartBody.Part>()

    @Test
    internal fun shouldGetProfile(@Random profile: Profile) = runBlocking {
        Mockito.`when`(gateway.get(profile.id)).thenReturn(profile)

        val fetchedProfile = service.getProfile(profile.id)

        assertThat(fetchedProfile, `is`(profile))
    }

    @Test
    internal fun shouldCreateProfile(@Random profile: Profile) = runBlocking {
        Mockito.`when`(gateway.create(profile)).thenReturn(profile)

        val fetchedProfile = service.createProfile(profile)

        assertThat(fetchedProfile, `is`(profile))
    }

    @Test
    internal fun shouldUpdateProfile(@Random profile: Profile) = runBlocking {
        service.updateProfile(profile)

        verify(gateway, times(1)).update(profile.id, profile)
    }

    @Test
    internal fun shouldUploadProfilePicture(@Mock file: File, @Random stringUri: String) = runBlocking {
        val filePart = createFormData("file", file.name, create(MediaType.parse("image/*"), file))

        service.uploadPicture(file)

        verify(gateway).uploadPicture(fileCaptor.capture())
        assertThat(fileCaptor.firstValue.body().contentLength(), `is`(filePart.body().contentLength()))
    }
}