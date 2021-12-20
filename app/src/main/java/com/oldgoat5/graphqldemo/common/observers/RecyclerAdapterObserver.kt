package com.oldgoat5.graphqldemo.common.observers

import androidx.recyclerview.widget.RecyclerView
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerAdapter
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerViewHolder

class RecyclerAdapterObserver<T, H : GraphQLRecyclerViewHolder<T>>(private val adapter: GraphQLRecyclerAdapter<T, H>) {

    fun onNext(items: List<T>) {
        adapter.setItems(items)
    }
}