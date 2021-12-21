package com.oldgoat5.graphqldemo.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class GraphQLRecyclerAdapter<T, H : GraphQLRecyclerViewHolder<T>, D : LazyDiffCallback<T>>
    : RecyclerView.Adapter<H>() {

    protected abstract val diffCallback: D

    private var existingItems = emptyList<T>()

    fun setItems(newItems: List<T>) {
        this.diffCallback.oldList = existingItems
        this.diffCallback.newList = newItems

        val diffResult = DiffUtil.calculateDiff(this.diffCallback, true)

        this.existingItems = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        this.existingItems[position].let { holder.onBind(it) }
    }

    override fun getItemCount(): Int {
        return this.existingItems.size
    }
}

abstract class LazyDiffCallback<T> : DiffUtil.Callback() {
    private var _oldList: List<T> = emptyList()
    private var _newList: List<T> = emptyList()

    var oldList: List<T>
        get() = _oldList
        set(value) {
            _oldList = value
        }

    var newList: List<T>
        get() = _newList
        set(value) {
            _newList = value
        }
}
