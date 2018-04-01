package com.nemov.materialrecycler

import io.reactivex.Observable

/**
 * Created by ynemov on 31.03.18.
 */
class Model: IModel {
    val model = IModel.create()

    override fun getProgramList(
            uuid: String,
            borderId: Int,
            direction: IPresenter.Direction): Observable<Program> {
        return model.getProgramList(uuid, borderId, direction)
    }

    companion object {
        data class Program(
                val items: List<Item>,
                val items_number: Int,
                val offset: Int,
                val total: Int,
                val hasMore: Int
        )
        data class Item(
                val id: Int,
                val icon: String,
                val name: String
        )
    }
}