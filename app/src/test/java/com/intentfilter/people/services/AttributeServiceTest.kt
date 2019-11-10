package com.intentfilter.people.services

import com.intentfilter.people.gateways.AttributeServiceGateway
import com.intentfilter.people.models.SingleChoiceAttributes
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
class AttributeServiceTest {
    @Mock
    lateinit var gateway: AttributeServiceGateway

    @InjectMocks
    lateinit var service: AttributeService

    @Test
    internal fun shouldGetAttributes(@Random attributes: SingleChoiceAttributes) = runBlocking {
        `when`(gateway.getAttributes()).thenReturn(attributes)

        val fetchedAttributes = service.getAttributes()

        assertThat(fetchedAttributes, `is`(attributes))
    }
}