package com.oldgoat5.graphqldemo.language

import androidx.lifecycle.viewModelScope
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageInteractor
import com.oldgoat5.graphqldemo.api.countrylanguage.LanguageState
import com.oldgoat5.graphqldemo.common.GraphQLViewModel
import com.oldgoat5.graphqldemo.common.mapToStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
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

    fun getLanguageState(): StateFlow<LanguageState<CountryLanguageQuery.Data>> {
        return countryLanguageInteractor.getLanguageState()
    }

    fun getLanguageLoadingState(): StateFlow<CountryLanguageInteractor.Status> {
        return countryLanguageInteractor.getLanguageState()
            .mapToStateFlow(viewModelScope, CountryLanguageInteractor.Status.LOADING) { it.status }
    }

    //todo combine with isReverseSortEnabled
    fun getLanguages(): StateFlow<List<CountryLanguageQuery.Country?>> {
        return countryLanguageInteractor.getLanguageState()
            .mapToStateFlow(viewModelScope, emptyList()) {
                if (it.data?.countries != null) {
                    return@mapToStateFlow (it.data.countries.reversed())
                } else {
                    return@mapToStateFlow (emptyList<CountryLanguageQuery.Country?>())
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
