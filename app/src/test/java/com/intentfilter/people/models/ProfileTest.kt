package com.intentfilter.people.models

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.platform.commons.util.ClassLoaderUtils

internal class ProfileTest {
    @Test
    internal fun shouldDeserializeFromProperJson() {
        val sampleProfileResponse = ClassLoaderUtils.getDefaultClassLoader().getResource("sample-profile-response.json")
        val profile = Profile(
            "5dcefdb4bc4ae21008ddbd32", "Nishkarsh", "Nishkarsh Sharma",
            "https://i.pinimg.com/originals/2a/c4/cb/2ac4cbfec5d66d3c996c521969199e58.jpg", "2000-10-10",
            "4e21c17b-0b4f-42c7-9084-daddbe13bf9f", "4e21c17b-0b4f-42c7-9084-daddbe13bf9f",
            "4e21c17b-0b4f-42c7-9084-daddbe13bf9f", 1.25, "4e21c17b-0b4f-42c7-9084-daddbe13bf9f",
            "4e21c17b-0b4f-42c7-9084-daddbe13bf9f", "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            Coordinates("46W", "44E"), 2
        )

        val deserializeProfile = ObjectMapper().readValue<Profile>(sampleProfileResponse, Profile::class.java)

        assertThat(deserializeProfile, `is`(profile))
    }
}