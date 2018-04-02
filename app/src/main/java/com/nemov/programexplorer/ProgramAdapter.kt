package com.nemov.programexplorer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nemov.programexplorer.R
import com.squareup.picasso.Picasso

/**
 * Created by ynemov on 01.04.18.
 */
class ProgramAdapter(private var programList: ProgramModel.Companion.Program) : RecyclerView.Adapter<ProgramAdapter.ViewHolder>(), IAdapter {
    override fun getItemCount() = programList.items_number

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater
                    .from(parent?.context)
                    .inflate(R.layout.item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.imgIcon.loadUrl(programList.items[position].icon)
        holder?.txtName.text = programList.items[position].name
    }

    override fun prependAll(programs: ProgramModel.Companion.Program) {
        val itemCount = programs.items_number
        programList = programs append programList
        notifyItemRangeInserted(0, itemCount)
    }

    override fun appendAll(programs: ProgramModel.Companion.Program) {
        val positionStart = programList.items.size
        val itemCount = programs.items_number
        programList = programList append programs
        notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun getDataIDByAdapterPosition(index: Int) = programList.items[index].id
    override fun getFirstDataID() = programList.items[0].id
    override fun getLastDataID() = programList.items[programList.items.size - 1].id

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgIcon = itemView.findViewById<ImageView>(R.id.imgIcon)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}

private fun ImageView.loadUrl(url: String) {
    Picasso.get().load(url).into(this)
}
