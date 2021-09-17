package com.beyondidentity.authenticator.sdk.android.authenticator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.authenticator.sdk.android.utils.DEFAULT_SHARED_PREFS_KEY
import com.beyondidentity.authenticator.sdk.android.utils.RetrofitBuilder
import com.beyondidentity.authenticator.sdk.android.utils.SHARED_PREF_SESSION_KEY
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch

class AuthenticatorHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val balanceTextView = findViewById<MaterialTextView>(R.id.balance)

        getSharedPreferences(DEFAULT_SHARED_PREFS_KEY, MODE_PRIVATE)
            .getString(SHARED_PREF_SESSION_KEY, null)?.let { session ->
                lifecycleScope.launch {
                    val balanceResponse = RetrofitBuilder.ACME_API_SERVICE.getBalance(session = session)
                    balanceTextView.text =
                        "Hi ${balanceResponse.userName}\nYour balance is ${balanceResponse.balance}"
                }
            } ?: run {
            balanceTextView.text = "No session, nothing to show"
        }
    }
}
