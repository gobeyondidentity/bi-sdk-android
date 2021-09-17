package com.beyondidentity.embedded.embeddedui.ui

import android.view.View
import com.google.android.material.textfield.TextInputEditText

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

fun TextInputEditText.isValidEmail() = text.toString().isNotEmpty() && text.toString().contains("@")
