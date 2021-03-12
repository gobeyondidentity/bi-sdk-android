package com.beyondidentity.authenticator.sdk

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
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

    //    fun setPasswordlessLogInListener(loginUrl: String, redirectUrl: String) {
    //        setOnClickListener {
    //            val url = "$loginUrl?redirect=$redirectUrl"
    //            val builder = CustomTabsIntent.Builder()
    //
    //            val customTabsIntent = builder.build()
    //            customTabsIntent.launchUrl(context, Uri.parse(url))
    //        }
    //    }
}
