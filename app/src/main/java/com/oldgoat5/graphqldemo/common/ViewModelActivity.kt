package com.oldgoat5.graphqldemo.common

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class ViewModelActivity<T : ViewModel> : AppCompatActivity() {

    protected val viewModel: T

    abstract fun setUpViewModel(): T

    init {
        viewModel = this.setUpViewModel()
    }

}