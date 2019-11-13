package com.intentfilter.people.views.profile

import android.net.Uri
import com.intentfilter.people.extensions.InstantExecutorExtension
import com.intentfilter.people.models.Locations
import com.intentfilter.people.models.SingleChoiceAttributes
import com.intentfilter.people.services.AttributeService
import com.intentfilter.people.services.LocationService
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

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

    private lateinit var viewModel: ProfileViewModel

    @BeforeEach
    @ExperimentalCoroutinesApi
    internal fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = ProfileViewModel(attributeService, locationService, testCoroutineDispatcher)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetChoiceAttributes(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(it, `is`(attributes))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetLocations(@Random locations: Locations) = runBlockingTest {
        whenever(locationService.getLocations()).thenReturn(locations)

        viewModel.locations.observeForever {
            assertThat(it, `is`(locations))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetGenderOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(viewModel.getGenderOptions(), `is`(attributes.gender))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetEthnicityOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(viewModel.getEthnicityOptions(), `is`(attributes.ethnicity))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetFigureTypeOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(viewModel.getFigureTypeOptions(), `is`(attributes.figure))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetReligionOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(viewModel.getReligionOptions(), `is`(attributes.religion))
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetMaritalStatusOptions(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(viewModel.getMaritalStatusOptions(), `is`(attributes.maritalStatus))
        }
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
    @ExperimentalCoroutinesApi
    internal fun tearDown() {
        Dispatchers.resetMain()
    }
}