package com.nemov.programexplorer.programadapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nemov.programexplorer.R
import com.nemov.programexplorer.api.ProgramModel
import com.nemov.programexplorer.commons.adapter.ViewType
import com.nemov.programexplorer.commons.adapter.ViewTypeDelegateAdapter
import com.nemov.programexplorer.commons.loadUrl

/**
 * Created by ynemov on 4/2/18.
 */
class ProgramDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup) =
            ProgramViewHolder(LayoutInflater
                    .from(parent?.context)
                    .inflate(R.layout.item_layout, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ProgramViewHolder
        holder.bind(item as ProgramModel.Companion.Program)
    }

    inner class ProgramViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imgIcon = itemView.findViewById<ImageView>(R.id.imgIcon)
        private val txtName = itemView.findViewById<TextView>(R.id.txtName)

        fun bind(item: ProgramModel.Companion.Program) {
            imgIcon.loadUrl(item.icon)
            txtName.text = item.name
        }
    }
}