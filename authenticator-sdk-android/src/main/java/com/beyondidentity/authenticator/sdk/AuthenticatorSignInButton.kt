package com.beyondidentity.authenticator.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class AuthenticatorSignInButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        text = context.getString(R.string.authenticator_log_in_with_passwordless)
    }

    fun setPasswordlessLogInListener(redirectUrl: String) {
        setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://acme-cloud.byndid.com/start?redirect=$redirectUrl")))
        }
    }
}
