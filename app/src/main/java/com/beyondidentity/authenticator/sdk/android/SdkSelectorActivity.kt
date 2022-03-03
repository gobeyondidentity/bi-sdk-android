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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.authenticator.AuthenticatorSignInActivity
import com.beyondidentity.authenticator.sdk.android.embedded.EmbeddedUISdkLoginActivity
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedActivity
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SdkSelectorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiSdkAndroidTheme {
                Surface {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        SdkSelectorLayout()
                    }
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
            .padding(start = 24.dp, end = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current

        Text(
            text = "Beyond Identity Android SDKs",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp)
        )

        Text(
            text = "Beyond Identity provides the strongest authentication on the planet, " +
                    "eliminating passwords completely for customers at registration, login, " +
                    "and recovery, as well as from your database.",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        BiVersionText()

        Text(
            text = "Embedded SDK",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "The Embedded SDK is a holistic SDK solution offering the entire experience " +
                    "embedded in your product. Users will not need to download the Beyond Identity Authenticator. " +
                    "A set of functions are provided to you through the EmbeddedSdk singleton. This SDK supports OIDC and OAuth2."
        )
        Button(
            onClick = {
                context.startActivity(Intent(context, EmbeddedGetStartedActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "View Embedded SDK")
        }

        BiDivider(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Text(
            text = "Embedded UI SDK",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(text = "The EmbeddedUI SDK provides view wrappers around the Embedded SDK functions.")
        Button(
            onClick = { context.startActivity(Intent(context, EmbeddedUISdkLoginActivity::class.java)) },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "View Embedded UI SDK")
        }

        BiDivider(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Text(
            text = "Authenticator SDK",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
            Text(text = "View Authenticator SDK")
        }

        BiDivider(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_powered_by_bi),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)
        )
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BiSdkAndroidTheme {
        SdkSelectorLayout()
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NightModePreview() {
    BiSdkAndroidTheme {
        SdkSelectorLayout()
    }
}