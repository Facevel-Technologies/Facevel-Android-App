package com.facevel.inc.app.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.core.content.ContextCompat


fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

