package com.beyondidentity.authenticator.sdk

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout

class AuthView : LinearLayout {
    private lateinit var loginButton: ConstraintLayout
    private lateinit var signupButton: ConstraintLayout

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

    private fun initView() {
        inflate(context, R.layout.auth_view, this)

        loginButton = findViewById(R.id.auth_view_sign_in)
        signupButton = findViewById(R.id.auth_view_sign_up)
    }

    /**
     * Initialize the [AuthView]
     *
     * @param loginUri The https authorization endpoint your API backend provides.
     * @param signupButtonListener OnClickListener for the Sign Up Button.
     */
    fun initAuthView(
        loginUri: Uri,
        signupButtonListener: OnClickListener
    ) {
        loginButton.setOnClickListener { launchCustomTab(context, loginUri) }
        signupButton.setOnClickListener(signupButtonListener)
    }

    private fun launchCustomTab(context: Context, uri: Uri) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .build()

        customTabsIntent.launchUrl(context, uri)
    }
}
