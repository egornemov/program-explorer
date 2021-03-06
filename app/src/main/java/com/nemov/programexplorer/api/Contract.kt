package com.nemov.programexplorer.api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ynemov on 02.04.18.
 */
interface IView {
    fun showData()
    fun setResults(programs: ProgramModel.Companion.ProgramList)
    fun prependResults(programs: ProgramModel.Companion.ProgramList)
    fun appendResults(programs: ProgramModel.Companion.ProgramList)
}

val INITIAL_BORDER_ID = 0

interface IPresenter {
    enum class Direction(val direction: Int) {
        UP(-1), DOWN(1), NO_DIRECTION(0)
    }

    fun loadInitial(borderId: Int = INITIAL_BORDER_ID)
    fun loadPrevious(borderId: Int)
    fun loadNext(borderId: Int)
    fun dispose()
}

interface IModel {

    @GET("demo")
    fun getProgramList(
            @Query("serial_number") uuid: String,
            @Query("borderId") borderId: Int,
            @Query("direction") direction: Int): Observable<ProgramModel.Companion.ProgramList>

    companion object {
        fun create(): IModel {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://oll.tv/")
                    .build()

            return retrofit.create(IModel::class.java)
        }
    }
}

interface IAdapter {
    fun getDataIDByAdapterPosition(index: Int): Int
    fun getFirstDataID(): Int
    fun getLastDataID(): Int
    fun clearAndSetAll(programs: ProgramModel.Companion.ProgramList)
    fun prependAll(programs: ProgramModel.Companion.ProgramList)
    fun appendAll(programs: ProgramModel.Companion.ProgramList)
}