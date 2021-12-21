package com.oldgoat5.graphqldemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GraphQLDemoApplication : Application() {

    companion object {
        lateinit var instance: GraphQLDemoApplication
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}