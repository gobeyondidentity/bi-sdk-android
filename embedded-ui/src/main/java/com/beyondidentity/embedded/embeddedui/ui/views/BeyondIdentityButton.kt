package com.beyondidentity.embedded.embeddedui.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.continueWithBeyondIdentity

class BeyondIdentityButton : FrameLayout {
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

    fun setViewData(
        fm: FragmentManager,
    ) {
        setOnClickListener { continueWithBeyondIdentity(fm) }
    }

    private fun initView() {
        inflate(context, R.layout.bi_button, this)
    }
}
