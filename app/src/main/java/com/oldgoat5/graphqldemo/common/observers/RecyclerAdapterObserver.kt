package com.oldgoat5.graphqldemo.common.observers

import com.oldgoat5.graphqldemo.common.GraphQLRecyclerAdapter
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerViewHolder
import com.oldgoat5.graphqldemo.common.LazyDiffCallback
import timber.log.Timber

class RecyclerAdapterObserver<T, H : GraphQLRecyclerViewHolder<T>, D : LazyDiffCallback<T>>(
    private val adapter: GraphQLRecyclerAdapter<T, H, D>
) {

    fun onNext(items: List<T>) {
        adapter.setItems(items)
    }
}