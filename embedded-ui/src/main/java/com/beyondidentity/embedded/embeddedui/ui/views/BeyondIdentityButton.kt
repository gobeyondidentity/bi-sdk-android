package com.beyondidentity.embedded.embeddedui.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentityBeforeAuthFragment
import com.beyondidentity.embedded.embeddedui.ui.BeyondIdentityRegistrationFragment
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus
import com.beyondidentity.embedded.embeddedui.ui.utils.BiEventBus.BiEvent.BiEventError
import com.beyondidentity.embedded.sdk.EmbeddedSdk

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
        setOnClickListener {
            EmbeddedSdk.getCredentials { result ->
                result.onSuccess { credentials ->
                    if (credentials.isNotEmpty()) {
                        BeyondIdentityBeforeAuthFragment.newInstance()
                            .show(fm, BeyondIdentityBeforeAuthFragment.TAG)
                    } else {
                        val registrationFragment =
                            BeyondIdentityRegistrationFragment.newInstance(false)

                        registrationFragment.show(fm, BeyondIdentityRegistrationFragment.TAG)
                    }
                }
                result.onFailure {
                    BiEventBus.post(BiEventError(it))
                }
            }
        }
    }

    private fun initView() {
        inflate(context, R.layout.bi_button, this)
    }
}
