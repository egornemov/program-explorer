package com.nemov.programexplorer.commons

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by ynemov on 4/2/18.
 */
object AdapterConstants {
    val PROGRAMS = 1
    val LOADING = 2
}

interface ViewType {
    fun getViewType(): Int
}

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}