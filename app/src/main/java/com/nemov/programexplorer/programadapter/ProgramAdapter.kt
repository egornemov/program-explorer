package com.nemov.programexplorer.programadapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nemov.programexplorer.api.IAdapter
import com.nemov.programexplorer.api.ProgramModel
import com.nemov.programexplorer.commons.adapter.AdapterConstants
import com.nemov.programexplorer.commons.adapter.ViewType
import com.nemov.programexplorer.commons.adapter.ViewTypeDelegateAdapter

/**
 * Created by ynemov on 01.04.18.
 */
class ProgramAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IAdapter {
    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.PROGRAM, ProgramDelegateAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters.get(viewType).onCreateViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun clearAndSetAll(programs: ProgramModel.Companion.ProgramList) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items.addAll(programs.items)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size)
    }

    override fun prependAll(programs: ProgramModel.Companion.ProgramList) {
        val programList: List<ProgramModel.Companion.Program> = getProgramList()
        val itemCount = programs.items_number

        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(programs.items)
        items.addAll(programList)
        items.add(loadingItem)
        notifyItemRangeInserted(0, itemCount)
    }

    override fun appendAll(programs: ProgramModel.Companion.ProgramList) {
        val programList: List<ProgramModel.Companion.Program> = getProgramList()
        val positionStart = programList.size
        val nextPrograms = programs.items.subList(1, programs.items.size)
        val itemCount = nextPrograms.size

        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(programList)
        items.addAll(nextPrograms)
        items.add(loadingItem)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun getDataIDByAdapterPosition(index: Int) = getProgramList()[index].id

    override fun getFirstDataID(): Int {
        val programs = getProgramList()
        if (programs.isEmpty()) return 0
        return programs.first().id
    }
    override fun getLastDataID(): Int {
        val programs = getProgramList()
        if (programs.isEmpty()) return 0
        return programs.last().id
    }

    private fun getProgramList(): List<ProgramModel.Companion.Program> =
            items.filter { it.getViewType() == AdapterConstants.PROGRAM }
                    .map { it as ProgramModel.Companion.Program }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}