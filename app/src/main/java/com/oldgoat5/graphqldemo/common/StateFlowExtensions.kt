package com.oldgoat5.graphqldemo.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, R> StateFlow<T>.mapToStateFlow(scope: CoroutineScope, default: R, map: (T) -> R): StateFlow<R> {
    return map(map).stateIn(scope, SharingStarted.Lazily, default)
}