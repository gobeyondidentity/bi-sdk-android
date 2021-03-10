package com.beyondidentity.authenticator.sdk.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val uri = intent.data!!
        Log.d("Home", "We got our sessions ${uri.getQueryParameter("session")}")
    }
}