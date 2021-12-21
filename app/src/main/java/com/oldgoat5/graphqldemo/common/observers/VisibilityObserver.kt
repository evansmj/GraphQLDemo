package com.oldgoat5.graphqldemo.common.observers

import android.view.View

class VisibilityObserver(private val view: View) {

    fun onNext(visible: Boolean) = when {
        visible -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}