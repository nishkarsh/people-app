package com.intentfilter.people.providers

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SharedPreferencesModuleTest {
    @Mock
    lateinit var context: Context

    @InjectMocks
    lateinit var sharedPreferencesModule: SharedPreferencesModule

    @Test
    internal fun shouldProvideSharedPreferences(@Mock sharedPreferences: SharedPreferences) {
        whenever(context.getSharedPreferences("intentfilter", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)

        val providedSharedPreferences = sharedPreferencesModule.providePreferences()

        assertThat(providedSharedPreferences, `is`(instanceOf(SharedPreferences::class.java)))
    }
}