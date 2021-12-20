package com.oldgoat5.graphqldemo.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class GraphQLRecyclerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: T)
}