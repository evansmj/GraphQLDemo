package com.oldgoat5.graphqldemo.api.countrylanguage

import com.oldgoat5.CountryLanguageQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject

class CountryLanguageInteractor @Inject constructor(private val countryLanguageService: CountryLanguageService) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    private val languageState = MutableStateFlow(
        LanguageState<CountryLanguageQuery.Data>(Status.LOADING, null, null)
    )

    fun getLanguageState(): StateFlow<LanguageState<CountryLanguageQuery.Data>> {
        return languageState
    }

    suspend fun fetchCountryLanguage() {
        languageState.value = LanguageState.loading()

        countryLanguageService.fetchCountryLanguage()
            .catch {
                languageState.value = LanguageState.error(Exception(it))
            }
            .collect {
                languageState.value = LanguageState.success(it.data)
            }
    }
}

data class LanguageState<out T>(val status: CountryLanguageInteractor.Status, val data: T?, val exception: Exception?) {

    companion object {
        fun <T> success(data: T?): LanguageState<T> {
            return LanguageState(CountryLanguageInteractor.Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Exception): LanguageState<T> {
            return LanguageState(CountryLanguageInteractor.Status.ERROR, null, exception)
        }

        fun <T> loading(): LanguageState<T> {
            return LanguageState(CountryLanguageInteractor.Status.LOADING, null, null)
        }
    }
}