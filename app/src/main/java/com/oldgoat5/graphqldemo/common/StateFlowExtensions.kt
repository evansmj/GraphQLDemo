package com.oldgoat5.graphqldemo.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, R> StateFlow<T>.mapToStateFlow(scope: CoroutineScope, default: R, map: (T) -> R): StateFlow<R> {
    return map(map).stateIn(scope, SharingStarted.Lazily, default)
}

fun <T1, T2, R> StateFlow<T1>.combineToStateFlow(
    scope: CoroutineScope,
    default: R,
    combine: StateFlow<T2>,
    transform: suspend (T1, T2) -> R
): StateFlow<R> {
    return combine(combine, transform).stateIn(scope, SharingStarted.Lazily, default)
}