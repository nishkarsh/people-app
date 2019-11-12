package com.intentfilter.people.views.common.itemchooser

import com.intentfilter.people.extensions.InstantExecutorExtension
import com.intentfilter.people.models.NamedAttribute
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@Extensions(
    ExtendWith(MockitoExtension::class),
    ExtendWith(RandomBeansExtension::class),
    ExtendWith(InstantExecutorExtension::class)
)
internal class SingleAttributeChooserViewModelTest {
    @InjectMocks
    lateinit var viewModel: SingleAttributeChooserViewModel

    @BeforeEach
    @ExperimentalCoroutinesApi
    internal fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    internal fun shouldGetSelectedNamedAttribute(@Random attributeOne: NamedAttribute, @Random attributeTwo: NamedAttribute) {
        viewModel.setNamedAttributes(arrayOf(attributeOne, attributeTwo))

        viewModel.selectAttribute(1)

        viewModel.selectedAttribute.observeForever {
            assertThat(it, `is`(attributeTwo))
        }
    }

    @AfterEach
    @ExperimentalCoroutinesApi
    internal fun tearDown() {
        Dispatchers.resetMain()
    }
}