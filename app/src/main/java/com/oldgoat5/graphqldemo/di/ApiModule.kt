package com.oldgoat5.graphqldemo.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.oldgoat5.graphqldemo.GraphQLDemoApplication
import com.oldgoat5.graphqldemo.api.ApolloConfiguration
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageInteractor
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @JvmStatic
    @Singleton
    fun providesApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(ApolloConfiguration.KEY_COUNTRY_SERVER)
            .build()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun providesCountryLanguageService(): CountryLanguageService {
        return CountryLanguageService(providesApolloClient())
    }

    @Provides
    @JvmStatic
    @Singleton
    fun providesCountryLanguageInteractor(): CountryLanguageInteractor {
        return CountryLanguageInteractor(providesCountryLanguageService())
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providesContext(): Context {
        return GraphQLDemoApplication.instance.applicationContext
    }
}