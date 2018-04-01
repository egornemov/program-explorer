package com.nemov.materialrecycler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ynemov on 31.03.18.
 */
class Presenter(val view: IView, val uuid: String): IPresenter {
    val model = Model()
    var disposable: Disposable? = null

    override fun loadInitial(borderId: Int) {
        load(borderId, IPresenter.Direction.NO_DIRECTION)
    }

    override fun loadPrevious(borderId: Int) {
        load(borderId, IPresenter.Direction.UP)
    }

    override fun loadNext(borderId: Int) {
        load(borderId, IPresenter.Direction.DOWN)
    }

    override fun dispose() {
        disposable?.dispose()
    }

    fun load(borderId: Int, direction: IPresenter.Direction) {
        view.showLoading()

        disposable = model.getProgramList(uuid, borderId, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.addResults(it)
                    view.showData()
                }
    }
}