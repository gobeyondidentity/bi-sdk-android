package com.beyondidentity.authenticator.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class AuthenticatorSignInButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        text = context.getString(R.string.authenticator_sign_in_with_beyond_identity)
        setTextColor(context.getColor(android.R.color.white))
        background = ContextCompat.getDrawable(context, R.drawable.authenticator_sign_in_background)
        isAllCaps = false
        setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(context, R.drawable.ic_beyond_identity_icon),
            null,
            null,
            null
        )
        compoundDrawablePadding = 16
    }

    fun setPasswordlessLogInListener(loginUrl: String, redirectUrl: String) {
        setOnClickListener {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("$loginUrl?redirect=$redirectUrl")
                )
            )
        }
    }
}
