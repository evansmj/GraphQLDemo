package com.oldgoat5.graphqldemo.language

import com.apollographql.apollo3.annotations.ApolloExperimental
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageInteractor
import com.oldgoat5.graphqldemo.api.countrylanguage.LanguageState
import com.oldgoat5.test.CountryLanguageQuery_TestBuilder.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class LanguageViewModelTest {

    private lateinit var viewModel: LanguageViewModel
    private var mockCountryLanguageInteractor = mock<CountryLanguageInteractor>()

    @ExperimentalCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = LanguageViewModel(mockCountryLanguageInteractor)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @ApolloExperimental
    @Test
    fun languageReverseTest() = runTest {
        val data = CountryLanguageQuery.Data {
            countries = listOf(
                country {
                    code = "a"
                },
                country {
                    code = "b"
                },
                country {
                    code = "c"
                }
            )
        }

        mockCountryLanguageInteractor = mock {
            on { getLanguageState() } doReturn MutableStateFlow(
                LanguageState(CountryLanguageInteractor.Status.LOADING, data, null)
            )
        }

        viewModel = LanguageViewModel(mockCountryLanguageInteractor)

        viewModel.onCreate()

        var actual: List<CountryLanguageQuery.Country?> = viewModel.getLanguages().first()

        assertEquals(data.countries, actual)

        viewModel.setReverseSortEnabled(true)

        actual = viewModel.getLanguages().first()

        assertEquals(data.countries!!.reversed(), actual)
    }
}