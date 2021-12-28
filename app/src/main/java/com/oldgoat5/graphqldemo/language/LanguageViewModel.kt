package com.oldgoat5.graphqldemo.language

import androidx.lifecycle.viewModelScope
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageInteractor
import com.oldgoat5.graphqldemo.api.countrylanguage.LanguageState
import com.oldgoat5.graphqldemo.common.GraphQLViewModel
import com.oldgoat5.graphqldemo.common.combineToStateFlow
import com.oldgoat5.graphqldemo.common.mapToStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val countryLanguageInteractor: CountryLanguageInteractor
) : GraphQLViewModel() {

    private var isReverseSortEnabled = MutableStateFlow(false)

    override fun onCreate() {
        super.onCreate()
        getAllLanguages()
    }

    fun getLanguageLoadingState(): StateFlow<CountryLanguageInteractor.Status> {
        return countryLanguageInteractor.getLanguageState()
            .mapToStateFlow(viewModelScope, CountryLanguageInteractor.Status.LOADING) { it.status }
    }

    fun getLanguages(): StateFlow<List<CountryLanguageQuery.Country?>> {
        return countryLanguageInteractor.getLanguageState()
            .combineToStateFlow(
                viewModelScope,
                LanguagesIsReversePair(LanguageState.loading(), false),
                this.isReverseSortEnabled
            ) { languages, isReverse ->
                return@combineToStateFlow LanguagesIsReversePair(languages, isReverse)
            }
            .mapToStateFlow(viewModelScope, emptyList()) {
                if (it.languages.data?.countries != null && it.isReverse) {
                    return@mapToStateFlow (it.languages.data.countries.reversed())
                } else if (it.languages.data?.countries != null && !it.isReverse) {
                    return@mapToStateFlow (it.languages.data.countries)
                } else {
                    return@mapToStateFlow (emptyList())
                }
            }
    }

    fun setReverseSortEnabled(isChecked: Boolean) {
        this.isReverseSortEnabled.value = isChecked
    }

    private fun getAllLanguages() {
        viewModelScope.launch {
            countryLanguageInteractor.fetchCountryLanguage()
        }
    }
}

private data class LanguagesIsReversePair(
    val languages: LanguageState<CountryLanguageQuery.Data>,
    val isReverse: Boolean
)