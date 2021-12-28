package com.oldgoat5.graphqldemo.api.countrylanguage

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.oldgoat5.CountryLanguageQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryLanguageService @Inject constructor(private val apolloClient: ApolloClient) {

    fun fetchCountryLanguage(): Flow<ApolloResponse<CountryLanguageQuery.Data>> {
        return apolloClient.query(CountryLanguageQuery())
            .toFlow()
    }
}