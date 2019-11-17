package com.intentfilter.people.services

import com.intentfilter.people.gateways.ProfileServiceGateway
import com.intentfilter.people.models.Profile
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
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
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import retrofit2.Call
import retrofit2.Response
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
        `when`(gateway.get(profile.id)).thenReturn(profile)

        val fetchedProfile = service.getProfile(profile.id)

        assertThat(fetchedProfile, `is`(profile))
    }

    @Test
    internal fun shouldCreateProfile(@Random profile: Profile) = runBlocking {
        `when`(gateway.create(profile)).thenReturn(profile)

        val fetchedProfile = service.createProfile(profile)

        assertThat(fetchedProfile, `is`(profile))
    }

    @Test
    internal fun shouldUpdateProfile(@Random profile: Profile, @Mock call: Call<Response<Unit>>) = runBlocking {
        `when`(gateway.update(profile.id, profile)).thenReturn(call)

        service.updateProfile(profile)

        verify(call).execute()
    }

    @Test
    internal fun shouldUploadProfilePicture(@Mock file: File) = runBlocking {
        val filePart = createFormData("file", file.name, create(MediaType.parse("image/*"), file))

        service.uploadPicture(file)

        verify(gateway).uploadPicture(fileCaptor.capture())
        assertThat(fileCaptor.firstValue.body().contentLength(), `is`(filePart.body().contentLength()))
    }
}