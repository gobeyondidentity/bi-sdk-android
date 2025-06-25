@file:Suppress("ktlint:standard:max-line-length")

package com.beyondidentity.authenticator.sdk.android.embedded.authenticate

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.ResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.Spacer16
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import com.beyondidentity.authenticator.sdk.android.embedded.authenticate.EmbeddedAuthenticateEvents.AuthenticateEvent
import com.beyondidentity.authenticator.sdk.android.embedded.authenticate.EmbeddedAuthenticateViewModel.WebMode
import kotlinx.coroutines.launch

class EmbeddedAuthenticateActivity : FragmentActivity() {
    private val viewModel: EmbeddedAuthenticateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        EmbeddedAuthenticateScreen(this@EmbeddedAuthenticateActivity, viewModel)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is AuthenticateEvent -> authenticateMessage(event.result)
                }
            }
        }

        viewModel.handleIntent(this@EmbeddedAuthenticateActivity, intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        viewModel.handleIntent(this@EmbeddedAuthenticateActivity, intent)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.handleActivityResult(
            this@EmbeddedAuthenticateActivity,
            requestCode,
            resultCode,
            data
        )
    }

    private fun authenticateMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun EmbeddedAuthenticateScreen(activity: FragmentActivity, viewModel: EmbeddedAuthenticateViewModel) {
    EmbeddedAuthenticateLayout(
        state = viewModel.state,
        viewModel = viewModel,
        onGetAuthenticationContext = {
            viewModel.onGetAuthenticationContext()
        },
        onAuthenticateBeyondIdentity = {
            viewModel.onAuthenticateBeyondIdentity(activity)
        },
        onAuthenticateOktaSDK = {
            viewModel.onAuthenticateOktaSdk(activity)
        },
        onAuthenticateOktaWeb = {
            viewModel.onAuthenticateOktaWeb(activity)
        },
        onAuthenticateAuth0SDK = {
            viewModel.onAuthenticateAuth0Sdk(activity)
        },
        onAuthenticateAuth0Web = {
            viewModel.onAuthenticateAuth0Web(activity)
        },
        onAuthenticateUrlTextChange = viewModel::onAuthenticateUrlTextChange,
        onAuthenticate = {
            viewModel.onAuthenticate(activity, viewModel.state.authenticateUrl)
        },
        onAuthenticateEmailOtpTextChange = viewModel::onAuthenticateEmailOtpTextChange,
        onAuthenticateEmailOtp = {
            viewModel.onAuthenticateEmailOtp(viewModel.state.authenticateEmailOtpUrl)
        },
        onRedeemEmailOtpTextChange = viewModel::onRedeemEmailOtpTextChange,
        onRedeemEmailOtp = {
            viewModel.onRedeemEmailOtp(viewModel.state.redeemEmailOtpUrl)
        }
    )
}

@Composable
fun EmbeddedAuthenticateLayout(
    state: EmbeddedAuthenticateState,
    viewModel: EmbeddedAuthenticateViewModel,
    onGetAuthenticationContext: () -> Unit,
    onAuthenticateBeyondIdentity: () -> Unit,
    onAuthenticateOktaSDK: () -> Unit,
    onAuthenticateOktaWeb: () -> Unit,
    onAuthenticateAuth0SDK: () -> Unit,
    onAuthenticateAuth0Web: () -> Unit,
    onAuthenticateUrlTextChange: (String) -> Unit,
    onAuthenticate: () -> Unit,
    onAuthenticateEmailOtpTextChange: (String) -> Unit,
    onAuthenticateEmailOtp: () -> Unit,
    onRedeemEmailOtpTextChange: (String) -> Unit,
    onRedeemEmailOtp: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Authenticate",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .testTag("Authenticate Header")
        )

        BiVersionText()

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        AuthenticationContextLayout(
            state,
            onGetAuthenticationContext
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Authenticate1Layout(
            state,
            viewModel,
            onAuthenticateBeyondIdentity,
            onAuthenticateOktaSDK,
            onAuthenticateOktaWeb,
            onAuthenticateAuth0SDK,
            onAuthenticateAuth0Web
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Authenticate2Layout(
            state,
            onAuthenticateUrlTextChange,
            onAuthenticate
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        AuthenticateEmailOtpLayout(
            state,
            onAuthenticateEmailOtpTextChange,
            onAuthenticateEmailOtp,
            onRedeemEmailOtpTextChange,
            onRedeemEmailOtp
        )
    }
}

@Composable
fun AuthenticationContextLayout(state: EmbeddedAuthenticateState, onGetAuthenticationContext: () -> Unit) {
    Text(
        text = "Authentication Context",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp)
    )

    ResponseInputView(
        description = "Returns the Authentication Context for the current transaction.\n" +
            "The Authentication Context contains the Authenticator Config, Authentication Method Configuration, request origin, and the authenticating application.",
        buttonText = "Get Authentication Context",
        testTag = "Get Authentication Context",
        onSubmit = onGetAuthenticationContext,
        submitResult = state.getAuthenticationContextResult,
        progressEnabled = state.getAuthenticationContextProgress
    )
}

@Composable
fun Authenticate1Layout(
    state: EmbeddedAuthenticateState,
    viewModel: EmbeddedAuthenticateViewModel,
    onAuthenticateBeyondIdentity: () -> Unit,
    onAuthenticateOktaSDK: () -> Unit,
    onAuthenticateOktaWeb: () -> Unit,
    onAuthenticateAuth0SDK: () -> Unit,
    onAuthenticateAuth0Web: () -> Unit
) {
    Text(
        text = "Authenticate",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
    )

    Text(
        text = "Authenticate against a passkey bound to this device. " +
            "If more than one passkey is present, you must select a passkey during authentication."
    )

    Spacer16()

    Text(
        text = "Authenticate with Beyond Identity",
        style = MaterialTheme.typography.subtitle1
    )

    ResponseInputView(
        description = "Try authenticating with Beyond Identity as the primary IdP.",
        buttonText = "Authenticate with Beyond Identity",
        testTag = "Authenticate with Beyond Identity",
        onSubmit = onAuthenticateBeyondIdentity,
        submitResult = state.authenticateBeyondIdentityResult,
        progressEnabled = state.authenticateBeyondIdentityProgress
    )

    if (viewModel.mWebMode == WebMode.WebView) {
        Spacer16()

        Text(
            text = "Authenticate with Okta (SDK)",
            style = MaterialTheme.typography.subtitle1
        )

        ResponseInputView(
            description = "Try authenticating with Okta using Beyond Identity as a secondary IdP.",
            buttonText = "Authenticate with Okta",
            testTag = "Authenticate with Okta SDK",
            onSubmit = onAuthenticateOktaSDK,
            submitResult = state.authenticateOktaSDKResult,
            progressEnabled = state.authenticateOktaSDKProgress
        )
    }

    Spacer16()

    Text(
        text = "Authenticate with Okta (Web)",
        style = MaterialTheme.typography.subtitle1
    )

    ResponseInputView(
        description = "Try authenticating with Okta using Beyond Identity as a secondary IdP.",
        buttonText = "Authenticate with Okta",
        testTag = "Authenticate with Okta Web",
        onSubmit = onAuthenticateOktaWeb,
        submitResult = state.authenticateOktaWebResult,
        progressEnabled = state.authenticateOktaWebProgress
    )

    if (viewModel.mWebMode == WebMode.WebView) {
        Spacer16()

        Text(
            text = "Authenticate with Auth0 (SDK)",
            style = MaterialTheme.typography.subtitle1
        )

        ResponseInputView(
            description = "Try authenticating with Auth0 using Beyond Identity as a secondary IdP.",
            buttonText = "Authenticate with Auth0",
            testTag = "Authenticate with Auth0 SDK",
            onSubmit = onAuthenticateAuth0SDK,
            submitResult = state.authenticateAuth0SDKResult,
            progressEnabled = state.authenticateAuth0SDKProgress
        )
    }

    Spacer16()

    Text(
        text = "Authenticate with Auth0 (Web)",
        style = MaterialTheme.typography.subtitle1
    )

    ResponseInputView(
        description = "Try authenticating with Auth0 using Beyond Identity as a secondary IdP.",
        buttonText = "Authenticate with Auth0",
        testTag = "Authenticate with Auth0 Web",
        onSubmit = onAuthenticateAuth0Web,
        submitResult = state.authenticateAuth0WebResult,
        progressEnabled = state.authenticateAuth0WebProgress
    )

    Spacer16()
}

@Composable
fun Authenticate2Layout(
    state: EmbeddedAuthenticateState,
    onAuthenticateUrlTextChange: (String) -> Unit,
    onAuthenticate: () -> Unit
) {
    Text(
        text = "Authenticate",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp)
    )

    InteractionResponseInputView(
        description = "Authenticates against a passkey bound to the device. " +
            "If more than one passkey is present, you must select a passkey during authentication.",
        inputValue = state.authenticateUrl,
        inputHint = "Authenticate URL",
        onInputChanged = onAuthenticateUrlTextChange,
        buttonText = "Authenticate",
        testTag = "Authenticate URL",
        onSubmit = onAuthenticate,
        submitResult = state.authenticateResult,
        progressEnabled = state.authenticateProgress
    )
}

@Composable
fun AuthenticateEmailOtpLayout(
    state: EmbeddedAuthenticateState,
    onAuthenticateEmailOtpTextChange: (String) -> Unit,
    onAuthenticateEmailOtp: () -> Unit,
    onRedeemEmailOtpTextChange: (String) -> Unit,
    onRedeemEmailOtp: () -> Unit
) {
    Text(
        text = "Authenticate with Email Otp",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp)
    )

    InteractionResponseInputView(
        description = "Try authenticating with Email OTP using Beyond Identity",
        inputValue = state.authenticateEmailOtpUrl,
        inputHint = "Email",
        onInputChanged = onAuthenticateEmailOtpTextChange,
        buttonText = "Authenticate with Email OTP",
        testTag = "Authenticate with Email OTP",
        onSubmit = onAuthenticateEmailOtp,
        submitResult = state.authenticateEmailOtpResult,
        progressEnabled = state.authenticateEmailOtpProgress
    )

    InteractionResponseInputView(
        description = "Try redeeming the OTP in your Email using Beyond Identity",
        inputValue = state.redeemEmailOtpUrl,
        inputHint = "OTP",
        onInputChanged = onRedeemEmailOtpTextChange,
        buttonText = "Redeem OTP",
        testTag = "Redeem OTP",
        onSubmit = onRedeemEmailOtp,
        submitResult = state.redeemEmailOtpResult,
        progressEnabled = state.redeemEmailOtpProgress
    )
}

@Composable
@Preview(showBackground = true)
@Suppress("MoveLambdaOutsideParentheses")
fun AuthenticationContextPreview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            AuthenticationContextLayout(
                EmbeddedAuthenticateState(),
                {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@Suppress("MoveLambdaOutsideParentheses")
fun Authenticate1Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            Authenticate1Layout(
                EmbeddedAuthenticateState(),
                EmbeddedAuthenticateViewModel(),
                {},
                {},
                {},
                {},
                {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@Suppress("MoveLambdaOutsideParentheses")
fun Authenticate2Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            Authenticate2Layout(
                EmbeddedAuthenticateState(),
                {},
                {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@Suppress("MoveLambdaOutsideParentheses")
fun AuthenticateEmailOtpPreview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            AuthenticateEmailOtpLayout(
                EmbeddedAuthenticateState(),
                {},
                {},
                {},
                {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun EmbeddedAuthenticatePreview() {
    BiSdkAndroidTheme {
        EmbeddedAuthenticateLayout(
            EmbeddedAuthenticateState(),
            EmbeddedAuthenticateViewModel(),
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun EmbeddedAuthenticatePreviewDark() {
    BiSdkAndroidTheme {
        EmbeddedAuthenticateLayout(
            EmbeddedAuthenticateState(),
            EmbeddedAuthenticateViewModel(),
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}
