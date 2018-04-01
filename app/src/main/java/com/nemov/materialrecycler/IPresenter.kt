package com.nemov.materialrecycler

/**
 * Created by ynemov on 31.03.18.
 */
val INITIAL_BORDER_ID = 0

interface IPresenter {
    enum class Direction(direction: Int) {
        UP(-1), DOWN(1), NO_DIRECTION(0)
    }

    fun loadInitial(borderId: Int = INITIAL_BORDER_ID)
    fun loadPrevious(borderId: Int)
    fun loadNext(borderId: Int)
    fun dispose()
}