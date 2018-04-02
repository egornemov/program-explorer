package com.nemov.programexplorer.api

import com.nemov.programexplorer.commons.adapter.AdapterConstants
import com.nemov.programexplorer.commons.adapter.ViewType
import java.util.*

/**
 * Created by ynemov on 31.03.18.
 */
class ProgramModel : IModel {
    val model = IModel.create()

    override fun getProgramList(uuid: String, borderId: Int, direction: Int) = model.getProgramList(uuid, borderId, direction)

    companion object {
        data class ProgramList(
                val items: ArrayList<Program>,
                val items_number: Int,
                val offset: Int,
                val total: Int,
                val hasMore: Int
        ) {
            infix fun append(program: ProgramList): ProgramList {
                var joined = ArrayList<Program>()
                joined.addAll(items)
                joined.addAll(program.items)

                return ProgramList(
                        joined,
                        items_number + program.items_number,
                        offset,
                        total,
                        program.hasMore
                )
            }
        }
        data class Program(
                val id: Int,
                val icon: String,
                val name: String
        ) : ViewType {
            override fun getViewType() = AdapterConstants.PROGRAM
        }
    }
}