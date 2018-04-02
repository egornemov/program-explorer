package com.nemov.programexplorer.commons

import android.content.Context
import android.provider.Settings
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by ynemov on 4/2/18.
 */

fun getUuid(context: Context): String {
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

fun ImageView.loadUrl(url: String) {
    Picasso.get().load(url).into(this)
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)