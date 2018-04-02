package com.nemov.programexplorer

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import java.util.*
import android.support.v4.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity(), IView, ConnectivityReceiver.ConnectivityReceiverListener {
    private val BORDER_ID_KEY = "BORDER_ID_KEY"

    private val presenter by lazy {
        ProgramPresenter(this, getUuid(this))
    }

    private var borderId = 0
    private var isConnected = false
    
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var rvProgram: RecyclerView
    private lateinit var scrollListener: EndlessRecyclerOnScrollListener

    override fun showData() {
        scrollListener.setLoading(false)
        swipeContainer.setRefreshing(false)
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    override fun addResults(programs: ProgramModel.Companion.Program) {
        if (rvProgram.adapter == null) {
            rvProgram.adapter = ProgramAdapter(programs)
        }
    }

    override fun prependResults(programs: ProgramModel.Companion.Program) {
        (rvProgram.adapter as IAdapter).prependAll(programs)
    }

    override fun appendResults(programs: ProgramModel.Companion.Program) {
        (rvProgram.adapter as IAdapter).appendAll(programs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        ConnectivityReceiver.connectivityReceiverListener = this

        borderId = savedInstanceState?.getInt(BORDER_ID_KEY)?: 0

        rvProgram = findViewById<RecyclerView>(R.id.programList)
        rvProgram.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        scrollListener = object : EndlessRecyclerOnScrollListener(rvProgram.layoutManager) {
            override fun onLoadMore() {
                if (!isConnected) return
                val borderId = (rvProgram?.adapter as IAdapter).getLastDataID()
                presenter.loadNext(borderId)
            }
        }
        rvProgram.addOnScrollListener(scrollListener)

        swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener {
            if (isConnected) {
                val borderId = (rvProgram?.adapter as IAdapter).getFirstDataID()
                presenter.loadPrevious(borderId)
            }
        }
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
    }

    override fun onPause() {
        super.onPause()
        presenter.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            val firstVisible = rvProgram.findFirstVisiblePosition()
            if (rvProgram.adapter != null) {
                borderId = (rvProgram.adapter as IAdapter).getDataIDByAdapterPosition(firstVisible)
            }
            putInt(BORDER_ID_KEY, borderId)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    private fun showMessage(isConnected: Boolean) {
        this.isConnected = isConnected
        if (isConnected && rvProgram.adapter == null) {
            presenter.loadInitial(borderId ?: 0)
        } else if (!isConnected) {
            Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun getUuid(context: Context): String {
    try {
        val androidId = Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)
        return UUID.fromString(androidId).toString()
    } catch (ignore: Exception) {
        return UUID.randomUUID().toString()
    }

}

fun RecyclerView.findFirstVisiblePosition(): Int {
    val child = getChildAt(0)
    return if (child == null) RecyclerView.NO_POSITION else getChildAdapterPosition(child)
}