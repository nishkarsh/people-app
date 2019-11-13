package com.intentfilter.people.services

import com.intentfilter.people.gateways.LocationServiceGateway
import com.intentfilter.people.models.Locations
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
class LocationServiceTest {
    @Mock
    lateinit var gateway: LocationServiceGateway

    @InjectMocks
    lateinit var service: LocationService

    @Test
    internal fun shouldGetLocations(@Random locations: Locations) = runBlocking {
        `when`(gateway.getLocations()).thenReturn(locations)

        val fetchedLocations = service.getLocations()

        assertThat(fetchedLocations, `is`(locations))
    }
}