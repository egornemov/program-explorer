package com.nemov.programexplorer

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import android.support.v4.widget.SwipeRefreshLayout
import com.nemov.programexplorer.api.IAdapter
import com.nemov.programexplorer.api.IView
import com.nemov.programexplorer.api.ProgramModel
import com.nemov.programexplorer.api.ProgramPresenter
import com.nemov.programexplorer.commons.ConnectivityReceiver
import com.nemov.programexplorer.commons.EndlessRecyclerOnScrollListener
import com.nemov.programexplorer.commons.findFirstVisiblePosition
import com.nemov.programexplorer.commons.getUuid
import com.nemov.programexplorer.programadapter.ProgramAdapter

class MainActivity : AppCompatActivity(), IView, ConnectivityReceiver.ConnectivityReceiverListener {
    private val BORDER_ID_KEY = "BORDER_ID_KEY"
    private val DEFAULT_BORDER_ID = 0

    private val presenter = ProgramPresenter(this, getUuid(this))
    private val connectivityReceiver = ConnectivityReceiver()

    private var borderId = 0
    private var isConnected = false

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var rvProgram: RecyclerView
    private lateinit var scrollListener: EndlessRecyclerOnScrollListener

    override fun showData() {
        scrollListener.setLoading(false)
        swipeContainer.setRefreshing(false)
    }

    override fun setResults(programs: ProgramModel.Companion.ProgramList) {
        (rvProgram.adapter as IAdapter).clearAndSetAll(programs)
    }

    override fun prependResults(programs: ProgramModel.Companion.ProgramList) {
        (rvProgram.adapter as IAdapter).prependAll(programs)
    }

    override fun appendResults(programs: ProgramModel.Companion.ProgramList) {
        (rvProgram.adapter as IAdapter).appendAll(programs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        borderId = savedInstanceState?.getInt(BORDER_ID_KEY)?: DEFAULT_BORDER_ID

        rvProgram = findViewById<RecyclerView>(R.id.programList)
        rvProgram.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvProgram.adapter = ProgramAdapter()
        scrollListener = object : EndlessRecyclerOnScrollListener(rvProgram.layoutManager) {
            override fun onLoadMore() {
                if (isConnected) {
                    val borderId = (rvProgram?.adapter as IAdapter).getLastDataID()
                    presenter.loadNext(borderId)
                } else {
                    scrollListener.setLoading(false)
                }
            }
        }
        rvProgram.addOnScrollListener(scrollListener)

        swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener {
            if (isConnected) {
                val borderId = (rvProgram?.adapter as IAdapter).getFirstDataID()
                presenter.loadPrevious(borderId)
            } else {
                swipeContainer.setRefreshing(false)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        super.onPause()
        presenter.dispose()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            val firstVisible = rvProgram.findFirstVisiblePosition()
            borderId = (rvProgram.adapter as IAdapter).getDataIDByAdapterPosition(
                    if (firstVisible < 0) DEFAULT_BORDER_ID else firstVisible
            )
            putInt(BORDER_ID_KEY, borderId)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        this.isConnected = isConnected
        if (isConnected) {
            presenter.loadInitial(borderId)
        } else {
            Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()
        }
    }
}