package com.intentfilter.people.providers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.intentfilter.people.gateways.AttributeServiceGateway
import com.intentfilter.people.gateways.LocationServiceGateway
import io.github.glytching.junit.extension.random.RandomBeansExtension
import okhttp3.OkHttpClient
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class GatewayModuleTest {

    @InjectMocks
    private lateinit var gatewayModule: GatewayModule

    @Test
    internal fun shouldProvideAttributeServiceGateway(@Mock okHttpClient: OkHttpClient) {
        val mapper = gatewayModule.provideObjectMapper()

        val providedGateway = gatewayModule.provideAttributeServiceGateway(okHttpClient, mapper)

        assertThat(providedGateway, `is`(instanceOf(AttributeServiceGateway::class.java)))
    }

    @Test
    internal fun shouldProvideLocationServiceGateway(@Mock okHttpClient: OkHttpClient) {
        val mapper = gatewayModule.provideObjectMapper()

        val providedGateway = gatewayModule.provideLocationServiceGateway(okHttpClient, mapper)

        assertThat(providedGateway, `is`(instanceOf(LocationServiceGateway::class.java)))
    }

    @Test
    internal fun shouldProvideConfiguredObjectMapper() {
        val objectMapper = gatewayModule.provideObjectMapper()

        assertTrue(objectMapper.isEnabled(MapperFeature.AUTO_DETECT_FIELDS))
        assertFalse(objectMapper.isEnabled(MapperFeature.AUTO_DETECT_GETTERS))
        assertFalse(objectMapper.isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS))
        assertFalse(objectMapper.isEnabled(MapperFeature.AUTO_DETECT_SETTERS))
        assertFalse(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES))
    }

    @Test
    internal fun shouldProvideConfiguredOkHttpClient() {
        val httpClient = gatewayModule.provideHttpClient()

        assertThat(httpClient, `is`(instanceOf(OkHttpClient::class.java)))
    }
}