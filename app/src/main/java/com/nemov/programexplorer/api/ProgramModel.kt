package com.nemov.programexplorer.api

import java.util.*

/**
 * Created by ynemov on 31.03.18.
 */
class ProgramModel : IModel {
    val model = IModel.create()

    override fun getProgramList(uuid: String, borderId: Int, direction: Int) = model.getProgramList(uuid, borderId, direction)

    companion object {
        data class Program(
                val items: ArrayList<Item>,
                val items_number: Int,
                val offset: Int,
                val total: Int,
                val hasMore: Int
        ) {
            infix fun append(program: Program): Program {
                var joined = ArrayList<Item>()
                joined.addAll(items)
                joined.addAll(program.items)

                return Program(
                        joined,
                        items_number + program.items_number,
                        offset,
                        total,
                        program.hasMore
                )
            }
        }
        data class Item(
                val id: Int,
                val icon: String,
                val name: String
        )
    }
}