package com.intentfilter.people.validators

import com.intentfilter.people.R
import com.intentfilter.people.models.ViewableProfile
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class ProfileFormValidatorTest {
    @Test
    internal fun shouldValidateProfile() {
        val viewableProfile = ViewableProfile(
            null, null,
            "http://10.0.2.2:9740/uploads/2ac4cbfec5d66d3c996c521969199e58.jpg", null, null,
            "Mixed", "Hindu", "125.0", "Athletic", null, "Applications Developer",
            "I am a cool person. I mean really. I like to try new things and would do anything at least once for the experiene of it.",
            null
        )

        val validationResult = ProfileFormValidator().validateFields(viewableProfile)

        assertThat(validationResult.size, `is`(6))
        assertThat(validationResult[R.id.inputDisplayName], `is`(R.string.error_required))
        assertThat(validationResult[R.id.inputActualFullName], `is`(R.string.error_required))
        assertThat(validationResult[R.id.viewBirthdayWrapper], `is`(R.string.error_required))
        assertThat(validationResult[R.id.viewGenderWrapper], `is`(R.string.error_required))
        assertThat(validationResult[R.id.viewMaritalStatusWrapper], `is`(R.string.error_required))
        assertThat(validationResult[R.id.viewLocation], `is`(R.string.error_required))
    }
}