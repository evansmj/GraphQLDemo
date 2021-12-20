package com.oldgoat5.graphqldemo.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.common.GraphQLViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val apolloClient: ApolloClient) : GraphQLViewModel() {

    private val languageState = MutableStateFlow(
        LanguageState<CountryLanguageQuery.Data>(Status.LOADING, null, null)
    )

    override fun onCreate() {
        super.onCreate()
        getAllLanguages()
    }

    fun getLanguageState(): StateFlow<LanguageState<CountryLanguageQuery.Data>> {
        return languageState
    }

    fun getAllLanguages() {
        languageState.value = LanguageState.loading()


        viewModelScope.launch {
            apolloClient.query(CountryLanguageQuery())
                .toFlow()
                .catch {
                    languageState.value = LanguageState.error(Exception(it))
                }
                .collect {
                    languageState.value = LanguageState.success(it.data)
                }
        }
    }
}

data class LanguageState<out T>(val status: Status, val data: T?, val exception: Exception?) {

    companion object {
        fun <T> success(data: T?): LanguageState<T> {
            return LanguageState(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Exception): LanguageState<T> {
            return LanguageState(Status.ERROR, null, exception)
        }

        fun <T> loading(): LanguageState<T> {
            return LanguageState(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}