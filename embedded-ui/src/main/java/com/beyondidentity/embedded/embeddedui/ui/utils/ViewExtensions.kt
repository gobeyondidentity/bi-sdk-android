package com.beyondidentity.embedded.embeddedui.ui

import android.view.View

/**
 * Sets [View] visibility to [View.VISIBLE]
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Sets [View] visibility to [View.INVISIBLE]
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Sets [View] visibility to [View.GONE]
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets visibility to [View.GONE] or [View.VISIBLE] based on boolean
 */
val Boolean.visibility: Int
    get() = if (this) View.VISIBLE else View.GONE
