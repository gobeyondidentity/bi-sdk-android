package com.beyondidentity.authenticator.sdk.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val balanceTextView = findViewById<MaterialTextView>(R.id.balance)

        val session = getSession(intent)

        session?.let { s ->
            lifecycleScope.launch {
                val balanceResponse = RetrofitBuilder.apiService.getBalance(session = s)
                balanceTextView.text = "Hi ${balanceResponse.userName}\nYour balance is ${balanceResponse.balance}"
            }
        }
    }

    private fun getSession(intent: Intent): String? {
        return intent.data?.getQueryParameter("session")?.let {
            Log.d("Home", "We got our sessions $it")
            it
        } ?: run {
            Log.d("Home", "Error getting the session")
            null
        }
    }
}
