package com.beyondidentity.embedded.embeddedui.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.gone
import com.beyondidentity.embedded.embeddedui.ui.visible
import com.google.android.material.progressindicator.CircularProgressIndicator

class BeyondIdentityLoadingButton : FrameLayout {
    private lateinit var buttonText: AppCompatTextView
    private lateinit var buttonProgressBar: CircularProgressIndicator

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView()
    }

    fun setText(text: String) {
        buttonText.text = text
    }

    fun setBackground(@DrawableRes drawableId: Int) {
        background = ContextCompat.getDrawable(context, drawableId)
    }

    fun showLoading() {
        buttonText.gone()
        buttonProgressBar.visible()
    }

    fun hideLoading() {
        buttonText.visible()
        buttonProgressBar.gone()
    }

    private fun initView() {
        inflate(context, R.layout.bi_loading_button, this)
        buttonText = findViewById(R.id.text)
        buttonProgressBar = findViewById(R.id.progress)
    }
}
