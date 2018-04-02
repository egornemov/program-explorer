package com.nemov.programexplorer

import android.support.v7.widget.RecyclerView

/**
 * Created by ynemov on 4/2/18.
 */
abstract class EndlessRecyclerOnScrollListener(private val layoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {
    private var loading = false // True if we are still waiting for the last set of data to load.
    private val visibleThreshold = 2 // The minimum amount of items to have below your current scroll position before loading more.
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy < 0) return
        // check for scroll down only
        visibleItemCount = recyclerView!!.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = recyclerView.findFirstVisiblePosition()

        // to make sure only one onLoadMore is triggered
        synchronized(this) {
            if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                // End has been reached, Do something
                onLoadMore()
                loading = true
            }
        }
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
    }

    abstract fun onLoadMore()
}