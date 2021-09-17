package com.beyondidentity.embedded.embeddedui.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.beyondidentity.embedded.embeddedui.R
import com.beyondidentity.embedded.embeddedui.ui.ActionType.Authentication
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
        authenticationListener: OnAuthenticationListener,
    ) {
        setOnClickListener {
            EmbeddedSdk.getCredentials { result ->
                result.onSuccess { credentials ->
                    if (credentials.isNotEmpty()) {
                        val authFm = BeyondIdentityActionHandlerFragment
                            .newInstance(Authentication)
                        authFm.onAuthenticationListener = authenticationListener

                        authFm.show(fm, BeyondIdentityActionHandlerFragment.TAG)
                    } else {
                        BeyondIdentityRegistrationFragment
                            .newInstance()
                            .show(fm, BeyondIdentityRegistrationFragment.TAG)
                    }
                }
                result.onFailure { }
            }
        }
    }

    private fun initView() {
        inflate(context, R.layout.bi_button, this)
    }
}
