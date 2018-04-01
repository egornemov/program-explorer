package com.nemov.materialrecycler

/**
 * Created by ynemov on 31.03.18.
 */
interface IView {
    fun showData()
    fun showLoading()
    fun addResults(programs: Model.Companion.Program)
}