package com.oldgoat5.graphqldemo.common

import androidx.lifecycle.ViewModel

abstract class GraphQLViewModel : ViewModel() {

    open fun onCreate() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    open fun onDestroy() {
    }
}