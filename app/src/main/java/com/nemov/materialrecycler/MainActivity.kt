package com.nemov.materialrecycler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity(), IView {
    val presenter by lazy {
        Presenter(this, getUuid())
    }
    lateinit var testTxt: TextView
    lateinit var programs: Model.Companion.Program

    override fun showData() {
        testTxt.setText(programs.toString())
    }

    override fun showLoading() {
        testTxt.setText("Loading...")
    }

    override fun addResults(programs: Model.Companion.Program) {
        this.programs = programs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testTxt = findViewById(R.id.test_txt)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadInitial()
    }

    override fun onPause() {
        super.onPause()
        presenter.dispose()
    }

    fun getUuid(): String {
        try {
            val androidId = Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)
            return UUID.fromString(androidId).toString()
        } catch (ignore: Exception) {
            return UUID.randomUUID().toString()
        }

    }
}
