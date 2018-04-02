package com.nemov.programexplorer.programview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nemov.programexplorer.R
import com.nemov.programexplorer.commons.adapter.ViewType
import com.nemov.programexplorer.commons.adapter.ViewTypeDelegateAdapter
import com.nemov.programexplorer.commons.inflate

/**
 * Created by ynemov on 4/2/18.
 */
class LoadingDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_loading))
}