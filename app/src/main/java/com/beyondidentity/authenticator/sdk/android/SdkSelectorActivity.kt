package com.beyondidentity.authenticator.sdk.android

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.authenticator.AuthenticatorSignInActivity
import com.beyondidentity.authenticator.sdk.android.embedded.EmbeddedAllFunctionalityActivity
import com.beyondidentity.authenticator.sdk.android.embedded.EmbeddedLoginActivity
import com.beyondidentity.authenticator.sdk.android.embedded.ui.theme.BisdkandroidTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SdkSelectorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BisdkandroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SdkSelectorLayout()
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun SdkSelectorLayout() {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current

        Text(
            text = "Beyond Identity Android SDKs",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Beyond Identity provides the strongest authentication on the planet, eliminating passwords completely for customers at registration, login, and recovery, as well as from your database.",
            modifier = Modifier.padding(top = 16.dp)
        )

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "The Embedded SDK is a holistic SDK solution offering the entire experience embedded in your product. Users will not need to download the Beyond Identity Authenticator. A set of functions are provided to you through the EmbeddedSdk singleton. This SDK supports OIDC and OAuth2.")
        Button(
            onClick = {
                context.startActivity(Intent(context, EmbeddedAllFunctionalityActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Embedded SDK")
        }

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "The EmbeddedUI SDK provides view wrappers around the Embedded SDK functions.")
        Button(
            onClick = { context.startActivity(Intent(context, EmbeddedLoginActivity::class.java)) },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Embedded SDK UI")
        }

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "Embed Passwordless Authentication into your Android app using the Beyond Identity Authenticator.")
        Button(
            onClick = {
                context.startActivity(Intent(context, AuthenticatorSignInActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Authenticator SDK")
        }

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_powered_by_bi),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BisdkandroidTheme {
        SdkSelectorLayout()
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NightModePreview() {
    BisdkandroidTheme {
        SdkSelectorLayout()
    }
}