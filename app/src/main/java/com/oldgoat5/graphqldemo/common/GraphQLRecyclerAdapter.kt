package com.oldgoat5.graphqldemo.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GraphQLRecyclerAdapter<T, H: GraphQLRecyclerViewHolder<T>> : RecyclerView.Adapter<H>() {

    protected var itemList = emptyList<T>()

    fun setItems(items: List<T>) {
        this.itemList = items
        notifyDataSetChanged() //todo use real comparisons
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        this.itemList[position]?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int {
        return this.itemList.size
    }
}