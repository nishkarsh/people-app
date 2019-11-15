package com.intentfilter.people.utilities

import android.content.SharedPreferences
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class PreferencesTest {
    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @InjectMocks
    lateinit var preferences: Preferences

    @Test
    internal fun shouldReturnFalseIfProfileDoesNotExist() {
        `when`(sharedPreferences.getString(Preferences.PROFILE_ID, null)).thenReturn(null)

        assertFalse(preferences.isProfileCreated())
    }

    @Test
    internal fun shouldReturnTrueIfProfileDoesExists(@Random profileId: String) {
        `when`(sharedPreferences.getString(Preferences.PROFILE_ID, null)).thenReturn(profileId)

        assertTrue(preferences.isProfileCreated())
    }

    @Test
    internal fun shouldReturnProfileId(@Random profileId: String) {
        `when`(sharedPreferences.getString(Preferences.PROFILE_ID, null)).thenReturn(profileId)

        assertThat(preferences.getProfile(), `is`(profileId))
    }
}