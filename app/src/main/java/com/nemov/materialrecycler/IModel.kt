package com.nemov.materialrecycler

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ynemov on 31.03.18.
 */
interface IModel {

    @GET("demo")
    fun getProgramList(
            @Query("serial_number") uuid: String,
            @Query("borderId") borderId: Int,
            @Query("direction") direction: IPresenter.Direction): Observable<Model.Companion.Program>

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