package com.intentfilter.people.adapters

import com.fasterxml.jackson.databind.ObjectMapper
import com.intentfilter.people.models.*
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.util.ClassLoaderUtils
import kotlin.reflect.KClass

internal class ViewableProfileAdapterTest {
    private lateinit var classLoader: ClassLoader
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        classLoader = ClassLoaderUtils.getDefaultClassLoader()
        objectMapper = ObjectMapper()
    }

    @Test
    internal fun shouldConvertProfileToViewableProfile() {
        val profile = Profile(
            "5dcefdb4bc4ae21008ddbd32", "Nishkarsh", "Nishkarsh Sharma",
            "https://i.pinimg.com/originals/2a/c4/cb/2ac4cbfec5d66d3c996c521969199e58.jpg", "2000-10-10",
            "8f9d76ad-2c6b-4a98-8496-6165a2770a5e", "44d17f32-f0c9-4bd0-801e-fd9b63d54342",
            "9bf8e0ef-11ea-4fe3-bdd4-e6d29fe406cb", 1.25, "ae32676c-4275-4536-a6e0-188100b97148",
            "5a837767-7a11-487c-a243-7451c7b14c03", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            Coordinates("5°19'N", "4°02'W"), 2
        )
        val viewableProfile = ViewableProfile(
            "Nishkarsh",
            "Nishkarsh Sharma",
            "https://i.pinimg.com/originals/2a/c4/cb/2ac4cbfec5d66d3c996c521969199e58.jpg",
            "2000-10-10",
            "Male",
            "Mixed",
            "Hindu",
            1.25,
            "Athletic",
            "Never Married",
            "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            "Abidjan"
        )

        val choiceAttributes = readObjectsFromFile("sample-choice-attributes.json", SingleChoiceAttributes::class)
        val locations = readObjectsFromFile("cities-slice.json", Locations::class)

        val convertedProfile = ViewableProfileAdapter.convert(profile, locations, choiceAttributes)

        assertThat(convertedProfile, `is`(viewableProfile))
    }

    private fun <T : Any> readObjectsFromFile(path: String, kClass: KClass<T>): T {
        return objectMapper.readValue<T>(classLoader.getResource(path), kClass.java)
    }
}