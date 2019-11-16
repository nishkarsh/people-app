package com.intentfilter.people.views.profile

import android.net.Uri
import com.intentfilter.people.adapters.ViewableProfileAdapter
import com.intentfilter.people.extensions.InstantExecutorExtension
import com.intentfilter.people.extensions.getOrAwaitValue
import com.intentfilter.people.models.Locations
import com.intentfilter.people.models.Profile
import com.intentfilter.people.models.SingleChoiceAttributes
import com.intentfilter.people.models.ViewableProfile
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
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.jupiter.MockitoExtension

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

        viewModel.triggerFetch()

        assertThat(viewModel.error.value, `is`(errorMessage))
    }

    @Test
    fun shouldGetChoiceAttributes(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.choiceAttributes.value, `is`(attributes))
    }

    @Test
    fun shouldGetLocations(@Random locations: Locations) = runBlockingTest {
        whenever(locationService.getLocations()).thenReturn(locations)

        viewModel.triggerFetch()

        assertThat(viewModel.locations.value, `is`(locations))
    }

    @Test
    fun shouldGetProfileWhenProfileIdExistInPreferences(@Random profile: Profile) = runBlockingTest {
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)

        viewModel.triggerFetch()

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
        whenever(viewableProfileAdapter.convert(profile, locations, attributes)).thenReturn(viewableProfile)

        viewModel.triggerFetch()

        assertThat(viewModel.viewableProfile.getOrAwaitValue(), `is`(viewableProfile))
    }

    @Test
    internal fun shouldNotAttemptConvertToViewableProfileWhenLocationsNotAvailable(
        @Random attributes: SingleChoiceAttributes, @Random profile: Profile
    ) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)
        whenever(preferences.getProfile()).thenReturn(profile.id)
        whenever(profileService.getProfile(profile.id)).thenReturn(profile)

        viewModel.triggerFetch()

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

        viewModel.triggerFetch()

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

        viewModel.triggerFetch()

        verifyNoMoreInteractions(viewableProfileAdapter)
        viewModel.viewableProfile.observeForever {
            assertNull(it)
        }
    }

    @Test
    fun shouldGetGenderOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.getGenderOptions(), `is`(attributes.gender))
    }

    @Test
    fun shouldGetEthnicityOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.getEthnicityOptions(), `is`(attributes.ethnicity))
    }

    @Test
    fun shouldGetFigureTypeOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.getFigureTypeOptions(), `is`(attributes.figure))
    }

    @Test
    fun shouldGetReligionOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.getReligionOptions(), `is`(attributes.religion))
    }

    @Test
    fun shouldGetMaritalStatusOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.triggerFetch()

        assertThat(viewModel.getMaritalStatusOptions(), `is`(attributes.maritalStatus))
    }

    @Test
    internal fun shouldSetProfilePictureWhenUriNotNull(@Mock uri: Uri) {
        viewModel.setProfilePicture(uri)

        assertThat(viewModel.profilePicture.value, `is`(uri))
    }

    @Test
    internal fun shouldNotSetProfilePictureWhenUriNull(@Mock uri: Uri) {
        viewModel.setProfilePicture(uri)

        viewModel.setProfilePicture(null)

        assertNotNull(viewModel.profilePicture.value)
    }

    @AfterEach
    internal fun tearDown() {
        Dispatchers.resetMain()
    }
}