package com.nemov.programexplorer.api

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ynemov on 31.03.18.
 */
class ProgramPresenter(val view: IView, val uuid: String) : IPresenter {
    private val model = ProgramModel()
    private var disposable: Disposable? = null

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
        disposable = model.getProgramList(uuid, borderId, direction.direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe {
                    when(direction) {
                        IPresenter.Direction.NO_DIRECTION -> view.setResults(it)
                        IPresenter.Direction.UP -> view.prependResults(it)
                        IPresenter.Direction.DOWN -> view.appendResults(it)
                    }
                    view.showData()
                }
    }
}