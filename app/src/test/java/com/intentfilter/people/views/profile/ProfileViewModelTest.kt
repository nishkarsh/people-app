package com.intentfilter.people.views.profile

import android.net.Uri
import com.intentfilter.people.adapters.ViewableProfileAdapter
import com.intentfilter.people.extensions.InstantExecutorExtension
import com.intentfilter.people.extensions.getOrAwaitValue
import com.intentfilter.people.models.*
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
import com.intentfilter.people.services.ProfileService
import com.intentfilter.people.utilities.Preferences
import com.nhaarman.mockitokotlin2.whenever
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File

@ExperimentalCoroutinesApi
@Extensions(
    ExtendWith(MockitoExtension::class),
    ExtendWith(RandomBeansExtension::class),
    ExtendWith(InstantExecutorExtension::class)
)
internal class ProfileViewModelTest {
    @Mock
    lateinit var attributeService: AttributeService
    @Mock
    lateinit var locationService: LocationService
    @Mock
    lateinit var profileService: ProfileService
    @Mock
    lateinit var preferences: Preferences
    @Mock
    lateinit var viewableProfileAdapter: ViewableProfileAdapter

    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: ProfileViewModel

    @BeforeEach
    internal fun setUp() {
        coroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(coroutineDispatcher)

        viewModel = ProfileViewModel(
            attributeService, locationService, profileService, preferences, viewableProfileAdapter, coroutineDispatcher
        )
    }

    @Test
    internal fun shouldFetchDuringInit(
        @Random attributes: SingleChoiceAttributes, @Random locations: Locations, @Random profile: Profile
    ) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)

        val viewModel = ProfileViewModel(
            attributeService, locationService, profileService, preferences, viewableProfileAdapter, coroutineDispatcher
        )

        assertThat(viewModel.choiceAttributes.value, `is`(attributes))
        assertThat(viewModel.locations.value, `is`(locations))
        assertThat(viewModel.profile.value, `is`(profile))
    }

    @Test
    internal fun shouldPostErrorWhenExceptionOccursOnFetch(@Random errorMessage: String) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenThrow(RuntimeException(errorMessage))

        val restResponse = viewModel.initiateSync()

        assertThat(restResponse.error.value, `is`(instanceOf(Exception::class.java)))
        assertThat(restResponse.error.value?.message, `is`(errorMessage))
    }

    @Test
    internal fun shouldSaveProfile(
        @Random viewableProfile: ViewableProfile, @Random profile: Profile, @Random current: Profile, @Random locations: Locations,
        @Random choiceAttributes: SingleChoiceAttributes, @Mock profilePicture: File, @Random filePath: FilePath
    ) = runBlockingTest {
        // given
        viewModel.selectedProfilePicture = profilePicture

        whenever(attributeService.getAttributes()).thenReturn(choiceAttributes)
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(profileService.uploadPicture(profilePicture)).thenReturn(filePath)
        whenever(preferences.getProfile()).thenReturn(current.id)
        whenever(profileService.getProfile(current.id)).thenReturn(current)
        whenever(viewableProfileAdapter.from(current, locations, choiceAttributes)).thenReturn(viewableProfile)
        whenever(viewableProfileAdapter.from(viewableProfile, locations, choiceAttributes, current)).thenReturn(profile)

        viewModel.initiateSync()
        viewModel.viewableProfile.getOrAwaitValue()

        // when
        val restResponse = viewModel.saveProfile()

        // then
        verify(profileService).updateProfile(profile)
        assertNotNull(restResponse.success.getOrAwaitValue())
        assertThat(profile.profilePicturePath, `is`(filePath.fileName))
    }

    @Test
    internal fun shouldNotOverwritePicturePathWhenFileNotSelectedSaveProfile(
        @Random viewableProfile: ViewableProfile, @Random profile: Profile, @Random current: Profile, @Random locations: Locations,
        @Random choiceAttributes: SingleChoiceAttributes, @Random path: String
    ) = runBlockingTest {
        // given
        profile.profilePicturePath = path
        whenever(attributeService.getAttributes()).thenReturn(choiceAttributes)
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(preferences.getProfile()).thenReturn(current.id)
        whenever(profileService.getProfile(current.id)).thenReturn(current)
        whenever(viewableProfileAdapter.from(current, locations, choiceAttributes)).thenReturn(viewableProfile)
        whenever(viewableProfileAdapter.from(viewableProfile, locations, choiceAttributes, current)).thenReturn(profile)

        viewModel.initiateSync()
        viewModel.viewableProfile.getOrAwaitValue()

        // when
        viewModel.saveProfile()

        // then
        verify(profileService).updateProfile(profile)
        assertThat(profile.profilePicturePath, `is`(path))

        verifyNoMoreInteractions(profileService)
    }

    @Test
    internal fun shouldInformAboutErrorWhenSavingProfile(
        @Random viewableProfile: ViewableProfile, @Random profile: Profile, @Random current: Profile, @Random locations: Locations,
        @Random choiceAttributes: SingleChoiceAttributes
    ) = runBlockingTest {
        // given
        whenever(attributeService.getAttributes()).thenReturn(choiceAttributes)
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(preferences.getProfile()).thenReturn(current.id)
        whenever(profileService.getProfile(current.id)).thenReturn(current)
        whenever(viewableProfileAdapter.from(current, locations, choiceAttributes)).thenReturn(viewableProfile)
        whenever(viewableProfileAdapter.from(viewableProfile, locations, choiceAttributes, current)).thenReturn(profile)
        whenever(profileService.updateProfile(profile)).thenThrow(RuntimeException())

        viewModel.initiateSync()
        viewModel.viewableProfile.getOrAwaitValue()

        // when
        val restResponse = viewModel.saveProfile()

        // then
        verify(profileService).updateProfile(profile)
        assertThat(restResponse.error.value, `is`(instanceOf(Exception::class.java)))
    }

    @Test
    fun shouldGetDetailsOnSync(@Random attributes: SingleChoiceAttributes, @Random locations: Locations, @Random profile: Profile) =
        runBlockingTest {
            whenever(attributeService.getAttributes()).thenReturn(attributes)
            whenever(locationService.getLocations()).thenReturn(locations)
            whenever(preferences.getProfile()).thenReturn(profile.id)
            whenever(profileService.getProfile(profile.id)).thenReturn(profile)

            viewModel.initiateSync()

            assertThat(viewModel.choiceAttributes.value, `is`(attributes))
            assertThat(viewModel.locations.value, `is`(locations))
            assertThat(viewModel.profile.value, `is`(profile))
        }

    @Test
    fun shouldNotAttemptGetProfileWhenProfileIdNotPresentInPreferences() = runBlockingTest {
        viewModel.profile.value

        verifyNoMoreInteractions(profileService)
    }

    @Test
    internal fun shouldPostToViewableProfileWhenAvailable(
        @Random attributes: SingleChoiceAttributes, @Random locations: Locations, @Random profile: Profile, @Random viewableProfile: ViewableProfile
    ) = runBlockingTest {
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(attributeService.getAttributes()).thenReturn(attributes)
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)
        whenever(viewableProfileAdapter.from(profile, locations, attributes)).thenReturn(viewableProfile)

        viewModel.initiateSync()

        assertThat(viewModel.viewableProfile.getOrAwaitValue(), `is`(viewableProfile))
    }

    @Test
    internal fun shouldNotAttemptConvertToViewableProfileWhenLocationsNotAvailable(
        @Random attributes: SingleChoiceAttributes, @Random profile: Profile
    ) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)

        viewModel.initiateSync()

        verifyNoMoreInteractions(viewableProfileAdapter)
        viewModel.viewableProfile.observeForever {
            assertNull(it)
        }
    }

    @Test
    internal fun shouldNotAttemptConvertToViewableProfileWhenChoiceAttributesNotAvailable(
        @Random locations: Locations, @Random profile: Profile
    ) = runBlockingTest {
        whenever(locationService.getLocations()).thenReturn(locations)
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)

        viewModel.initiateSync()

        verifyNoMoreInteractions(viewableProfileAdapter)
        viewModel.viewableProfile.observeForever {
            assertNull(it)
        }
    }

    @Test
    internal fun shouldNotAttemptConvertToViewableProfileWhenNotAvailable(
        @Random attributes: SingleChoiceAttributes, @Random locations: Locations
    ) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)
        whenever(locationService.getLocations()).thenReturn(locations)

        viewModel.initiateSync()

        verifyNoMoreInteractions(viewableProfileAdapter)
        viewModel.viewableProfile.observeForever {
            assertNull(it)
        }
    }

    @Test
    fun shouldGetOptionsOnSync(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.initiateSync()

        assertThat(viewModel.getGenderOptions(), `is`(attributes.gender))
        assertThat(viewModel.getEthnicityOptions(), `is`(attributes.ethnicity))
        assertThat(viewModel.getFigureTypeOptions(), `is`(attributes.figure))
        assertThat(viewModel.getReligionOptions(), `is`(attributes.religion))
        assertThat(viewModel.getMaritalStatusOptions(), `is`(attributes.maritalStatus))
    }

    @Test
    internal fun shouldSetProfilePictureWhenUriNotNull(@Mock uri: Uri, @Mock file: File) {
        viewModel.setProfilePicture(uri, file)

        assertThat(viewModel.viewableProfile.value?.profilePicturePath, `is`(uri.toString()))
        assertThat(viewModel.selectedProfilePicture, `is`(file))
    }

    @AfterEach
    internal fun tearDown() {
        Dispatchers.resetMain()
    }
}