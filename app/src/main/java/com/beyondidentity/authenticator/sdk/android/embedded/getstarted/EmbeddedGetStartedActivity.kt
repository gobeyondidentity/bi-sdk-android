@file:Suppress("ktlint:standard:max-line-length")

package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

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
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiTextWithChevron
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.Spacer16
import com.beyondidentity.authenticator.sdk.android.composeui.components.Spacer32
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import com.beyondidentity.authenticator.sdk.android.embedded.authenticate.EmbeddedAuthenticateActivity
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.Authenticate
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindPasskeyEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.ManagePasskeys
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.UrlValidation
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitDocsEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitSupportEvent
import com.beyondidentity.authenticator.sdk.android.embedded.managepasskeys.ManagePasskeysActivity
import com.beyondidentity.authenticator.sdk.android.embedded.urlvalidation.EmbeddedUrlValidationActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedGetStartedActivity : FragmentActivity() {
    private val viewModel: EmbeddedGetStartedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        EmbeddedGetStartedScreen(viewModel)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    ManagePasskeys -> launchActivity(event = event)
                    Authenticate -> launchActivity(event = event)
                    UrlValidation -> launchActivity(event = event)
                    is BindPasskeyEvent -> bindPasskeyMessage(event.result)
                    is VisitDocsEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    is VisitSupportEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                }
            }
        }
    }

    private fun bindPasskeyMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun launchActivity(event: EmbeddedGetStartedEvents) {
        when (event) {
            ManagePasskeys -> startActivity(Intent(this, ManagePasskeysActivity::class.java))
            Authenticate -> startActivity(Intent(this, EmbeddedAuthenticateActivity::class.java))
            UrlValidation -> startActivity(Intent(this, EmbeddedUrlValidationActivity::class.java))
            else -> Timber.d("noop")
        }
    }
}

@Composable
fun EmbeddedGetStartedScreen(viewModel: EmbeddedGetStartedViewModel) {
    EmbeddedGetStartedLayout(
        state = viewModel.state,
        onRegisterPasskeyUsernameTextChange = viewModel::onPasskeyBindingLinkUsernameTextChange,
        onRegisterPasskey = {
            viewModel.onRegisterPasskey(viewModel.state.registerUsername)
        },
        onRecoverPasskeyUsernameTextChange = viewModel::onRecoverPasskeyBindingLinkUsernameTextChange,
        onRecoverPasskey = {
            viewModel.onRecoverPasskey(viewModel.state.recoverUsername)
        },
        onBindPasskeyUrlTextChange = viewModel::onBindPasskeyUrlTextChange,
        onBindPasskey = {
            viewModel.onBindPasskey(viewModel.state.bindPasskeyUrl)
        },
        onNavigate = viewModel::onGetStartedEvent
    )
}

@Composable
fun EmbeddedGetStartedLayout(
    state: EmbeddedGetStartedState,
    onRegisterPasskeyUsernameTextChange: (String) -> Unit,
    onRegisterPasskey: () -> Unit,
    onRecoverPasskeyUsernameTextChange: (String) -> Unit,
    onRecoverPasskey: () -> Unit,
    onBindPasskeyUrlTextChange: (String) -> Unit,
    onBindPasskey: () -> Unit,
    onNavigate: (EmbeddedGetStartedEvents) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Embedded SDK",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .testTag("Embedded SDK Header")
        )

        BiVersionText()

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        BindPasskey1Layout(
            state,
            onRegisterPasskeyUsernameTextChange,
            onRegisterPasskey,
            onRecoverPasskeyUsernameTextChange,
            onRecoverPasskey
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        BindPasskey2Layout(
            state,
            onBindPasskeyUrlTextChange,
            onBindPasskey
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "SDK Functionality",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Explore the various functions available when a passkey exists on the device.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BiTextWithChevron(
            text = "Manage Passkeys",
            testTag = "Manage Passkeys",
            onClick = { onNavigate(ManagePasskeys) }
        )

        BiDivider()

        BiTextWithChevron(
            text = "Authenticate",
            testTag = "Authenticate",
            onClick = { onNavigate(Authenticate) }
        )

        BiDivider()

        BiTextWithChevron(
            text = "URL Validation",
            testTag = "URL Validation",
            onClick = { onNavigate(UrlValidation) }
        )

        BiDivider()

        Text(
            text = "Questions or issues?",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Read through our developer docs for more details on our embedded SDK or reach out to support.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BiTextWithChevron(
            text = "View Developer Docs",
            testTag = "View Developer Docs",
            onClick = { onNavigate(VisitDocsEvent("https://developer.beyondidentity.com".toUri())) }
        )

        BiDivider()

        BiTextWithChevron(
            text = "Visit Support",
            testTag = "Visit Support",
            onClick = {
                onNavigate(
                    VisitSupportEvent(
                        "https://join.slack.com/t/byndid/shared_invite/zt-1anns8n83-NQX4JvW7coi9dksADxgeBQ".toUri()
                    )
                )
            }
        )
    }
}

@Composable
fun BindPasskey1Layout(
    state: EmbeddedGetStartedState,
    onRegisterPasskeyUsernameTextChange: (String) -> Unit,
    onRegisterPasskey: () -> Unit,
    onRecoverPasskeyUsernameTextChange: (String) -> Unit,
    onRecoverPasskey: () -> Unit
) {
    Text(
        text = "Get Started",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
    )

    Text(
        text = "Bind Passkey".uppercase(),
        style = MaterialTheme.typography.subtitle2
    )

    InteractionResponseInputView(
        description = "To get started with using our embedded SDK sample app, " +
            "enter any username to bind a passkey to this device.",
        inputValue = state.registerUsername,
        inputHint = "Username",
        onInputChanged = onRegisterPasskeyUsernameTextChange,
        buttonText = "Bind Passkey",
        testTag = "Register Passkey",
        onSubmit = onRegisterPasskey,
        submitResult = state.registerResult,
        progressEnabled = state.registerProgress
    )

    Spacer32()

    Text(
        text = "Recover Passkey".uppercase(),
        style = MaterialTheme.typography.subtitle2
    )

    InteractionResponseInputView(
        description = "If you have an account with a passkey you can’t access anymore, " +
            "enter your username to recover your account and bind a passkey to this device.",
        inputValue = state.recoverUsername,
        inputHint = "Username",
        onInputChanged = onRecoverPasskeyUsernameTextChange,
        buttonText = "Recover Passkey",
        testTag = "Recover Passkey",
        onSubmit = onRecoverPasskey,
        submitResult = state.recoverResult,
        progressEnabled = state.recoverProgress
    )

    Spacer16()
}

@Composable
fun BindPasskey2Layout(
    state: EmbeddedGetStartedState,
    onBindPasskeyUrlTextChange: (String) -> Unit,
    onBindPasskey: () -> Unit
) {
    Text(
        text = "Bind Passkey",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp)
    )

    InteractionResponseInputView(
        description = "Paste the Bind Passkey URL you received in your email or generated through the API in order to bind a passkey.",
        inputValue = state.bindPasskeyUrl,
        inputHint = "Bind Passkey URL",
        onInputChanged = onBindPasskeyUrlTextChange,
        buttonText = "Bind Passkey",
        testTag = "Bind Passkey URL",
        onSubmit = onBindPasskey,
        submitResult = state.bindPasskeyResult,
        progressEnabled = state.bindPasskeyProgress
    )
}

@Composable
@Preview(showBackground = true)
fun BindPasskey1Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            BindPasskey1Layout(
                EmbeddedGetStartedState(),
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
fun BindPasskey2Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp)
        ) {
            BindPasskey2Layout(
                EmbeddedGetStartedState(),
                {},
                {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun EmbeddedGetStartedPreview() {
    BiSdkAndroidTheme {
        EmbeddedGetStartedLayout(
            EmbeddedGetStartedState(),
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
fun EmbeddedGetStartedPreviewDark() {
    BiSdkAndroidTheme {
        EmbeddedGetStartedLayout(
            EmbeddedGetStartedState(),
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
