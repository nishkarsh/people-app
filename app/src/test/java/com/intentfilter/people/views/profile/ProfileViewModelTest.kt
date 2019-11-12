package com.intentfilter.people.views.profile

import com.intentfilter.people.extensions.InstantExecutorExtension
import com.intentfilter.people.models.SingleChoiceAttributes
import com.intentfilter.people.services.AttributeService
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

    lateinit var viewModel: ProfileViewModel

    @BeforeEach
    @ExperimentalCoroutinesApi
    internal fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = ProfileViewModel(attributeService, testCoroutineDispatcher)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun shouldGetChoiceAttributes(@Random attributes: SingleChoiceAttributes) = runBlockingTest {
        whenever(attributeService.getAttributes()).thenReturn(attributes)

        viewModel.choiceAttributes.observeForever {
            assertThat(it, `is`(attributes))
        }
    }

    @AfterEach
    @ExperimentalCoroutinesApi
    internal fun tearDown() {
        Dispatchers.resetMain()
    }
}