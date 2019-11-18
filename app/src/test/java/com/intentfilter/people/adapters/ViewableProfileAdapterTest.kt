package com.intentfilter.people.adapters

import com.fasterxml.jackson.databind.ObjectMapper
import com.intentfilter.people.models.*
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.commons.util.ClassLoaderUtils
import kotlin.reflect.KClass

@ExtendWith(RandomBeansExtension::class)
internal class ViewableProfileAdapterTest {
    private lateinit var classLoader: ClassLoader
    private lateinit var objectMapper: ObjectMapper

    private lateinit var viewableProfileAdapter: ViewableProfileAdapter

    @BeforeEach
    internal fun setUp() {
        classLoader = ClassLoaderUtils.getDefaultClassLoader()
        objectMapper = ObjectMapper()

        viewableProfileAdapter = ViewableProfileAdapter()
    }

    @Test
    internal fun shouldConvertProfileToViewableProfile() {
        val profile = Profile(
            "5dcefdb4bc4ae21008ddbd32", "Nishkarsh", "Nishkarsh Sharma",
            "2ac4cbfec5d66d3c996c521969199e58.jpg", "2000-11-10",
            "8f9d76ad-2c6b-4a98-8496-6165a2770a5e", "44d17f32-f0c9-4bd0-801e-fd9b63d54342",
            "9bf8e0ef-11ea-4fe3-bdd4-e6d29fe406cb", 125.0, "ae32676c-4275-4536-a6e0-188100b97148",
            "5a837767-7a11-487c-a243-7451c7b14c03", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            Coordinates("5°19'N", "4°02'W"), 2
        )
        val viewableProfile = ViewableProfile(
            "Nishkarsh",
            "Nishkarsh Sharma",
            "http://10.0.2.2:9740/uploads/2ac4cbfec5d66d3c996c521969199e58.jpg",
            "10 November, 2000",
            "Male",
            "Mixed",
            "Hindu",
            "125.0",
            "Athletic",
            "Never Married",
            "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            "Abidjan"
        )

        val choiceAttributes = readObjectsFromFile("sample-choice-attributes.json", SingleChoiceAttributes::class)
        val locations = readObjectsFromFile("cities-slice.json", Locations::class)

        val convertedProfile = viewableProfileAdapter.from(profile, locations, choiceAttributes)

        assertThat(convertedProfile, `is`(viewableProfile))
    }

    @Test
    internal fun shouldConvertViewableProfileToProfile(@Random currentProfile: Profile) {
        val viewableProfile = ViewableProfile(
            "Nishkarsh", "Nishkarsh Sharma",
            "http://10.0.2.2:9740/uploads/2ac4cbfec5d66d3c996c521969199e58.jpg", "10 November, 2000",
            "Male", "Mixed", "Hindu", "125.0",
            "Athletic", "Never Married", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            "Abidjan"
        )

        val profile = Profile(
            currentProfile.id, "Nishkarsh", "Nishkarsh Sharma",
            "2ac4cbfec5d66d3c996c521969199e58.jpg", "2000-11-10",
            "8f9d76ad-2c6b-4a98-8496-6165a2770a5e", "44d17f32-f0c9-4bd0-801e-fd9b63d54342",
            "9bf8e0ef-11ea-4fe3-bdd4-e6d29fe406cb", 125.0, "ae32676c-4275-4536-a6e0-188100b97148",
            "5a837767-7a11-487c-a243-7451c7b14c03", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            Coordinates("5°19'N", "4°02'W"), currentProfile.version
        )

        val choiceAttributes = readObjectsFromFile("sample-choice-attributes.json", SingleChoiceAttributes::class)
        val locations = readObjectsFromFile("cities-slice.json", Locations::class)

        val convertedProfile = viewableProfileAdapter.from(viewableProfile, locations, choiceAttributes, currentProfile)

        assertThat(convertedProfile, `is`(profile))
    }

    @Test
    internal fun shouldNotBuildPathForProfilePictureWhenNull(@Random currentProfile: Profile) {
        val viewableProfile = ViewableProfile(
            "Nishkarsh", "Nishkarsh Sharma", null,
            "10 November, 2000", "Male", "Mixed", "Hindu", "125.0",
            "Athletic", "Never Married", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            "Abidjan"
        )

        val profile = Profile(
            currentProfile.id, "Nishkarsh", "Nishkarsh Sharma", null, "2000-11-10",
            "8f9d76ad-2c6b-4a98-8496-6165a2770a5e", "44d17f32-f0c9-4bd0-801e-fd9b63d54342",
            "9bf8e0ef-11ea-4fe3-bdd4-e6d29fe406cb", 125.0, "ae32676c-4275-4536-a6e0-188100b97148",
            "5a837767-7a11-487c-a243-7451c7b14c03", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            Coordinates("5°19'N", "4°02'W"), currentProfile.version
        )

        val choiceAttributes = readObjectsFromFile("sample-choice-attributes.json", SingleChoiceAttributes::class)
        val locations = readObjectsFromFile("cities-slice.json", Locations::class)

        val convertedProfile = viewableProfileAdapter.from(viewableProfile, locations, choiceAttributes, currentProfile)

        assertThat(convertedProfile, `is`(profile))
    }

    private fun <T : Any> readObjectsFromFile(path: String, kClass: KClass<T>): T {
        return objectMapper.readValue<T>(classLoader.getResource(path), kClass.java)
    }
}