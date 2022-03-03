package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.embedded.auth.EmbeddedAuthActivity
import com.beyondidentity.authenticator.sdk.android.embedded.extend.ExtendCredentialActivity
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.Authenticate
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.CredentialRegistration
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.ExtendCredentials
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.ManageCredentials
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitDocsEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitSupportEvent
import com.beyondidentity.authenticator.sdk.android.embedded.managecred.ManageCredentialsActivity
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedGetStartedActivity : ComponentActivity() {
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

        intent.data?.let { uri ->
            Toast.makeText(this, "Registering Credential...", Toast.LENGTH_LONG).show()
            viewModel.registerCredentialWithUrl(uri.toString())
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is VisitSupportEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    is VisitDocsEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    Authenticate,
                    ExtendCredentials,
                    ManageCredentials -> launchActivity(event = event)
                    is CredentialRegistration -> credentialRegistrationMessage(event.result)
                }
            }
        }
    }

    private fun credentialRegistrationMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun launchActivity(event: EmbeddedGetStartedEvents) {
        when (event) {
            Authenticate -> startActivity(Intent(this, EmbeddedAuthActivity::class.java))
            ExtendCredentials -> startActivity(Intent(this, ExtendCredentialActivity::class.java))
            ManageCredentials -> startActivity(Intent(this, ManageCredentialsActivity::class.java))
            else -> Timber.d("noop")
        }
    }
}

@Composable
fun EmbeddedGetStartedScreen(viewModel: EmbeddedGetStartedViewModel) {
    EmbeddedGetStartedLayout(
        state = viewModel.state,
        onRegistrationEmailTextChange = viewModel::onRegistrationEmailTextChange,
        onRegisterUser = viewModel::onRegisterUser,
        onRecoveryEmailTextChange = viewModel::onRecoverEmailTextChange,
        onRecoverUser = viewModel::onRecoverUser,
        onNavigate = viewModel::onGetStartedEvent,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmbeddedGetStartedLayout(
    state: EmbeddedGetStartedState,
    onRegistrationEmailTextChange: (String) -> Unit,
    onRegisterUser: () -> Unit,
    onRecoveryEmailTextChange: (String) -> Unit,
    onRecoverUser: () -> Unit,
    onNavigate: (EmbeddedGetStartedEvents) -> Unit,
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
        )

        BiVersionText()

        Text(
            text = "Get Started",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Register Credential".uppercase(),
            style = MaterialTheme.typography.subtitle2,
        )

        InteractionResponseInputView(
            description = "To get started with using our embedded SDK sample app, " +
                    "enter your email below for directions to register an account and credential.",
            inputValue = state.registerEmail,
            onInputChanged = onRegistrationEmailTextChange,
            buttonText = "Register Credential",
            onSubmit = onRegisterUser,
            submitResult = state.registerResult,
        )

        Text(
            text = "Recover Credential".uppercase(),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(top = 32.dp)
        )

        InteractionResponseInputView(
            description = "If you have an account with a credential you can’t access anymore, " +
                    "enter your email below for directions to recover your account and create a new credential." +
                    "\n\nNote: If you recover an account, it will deactivate all existing credentials attached to the account.",
            inputValue = state.recoverEmail,
            onInputChanged = onRecoveryEmailTextChange,
            buttonText = "Recover Account",
            onSubmit = onRecoverUser,
            submitResult = state.recoverResult,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "SDK Functionality",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Explore the various functions available in embedded SDK.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Manage Credentials",
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(ManageCredentials) })
        )
        BiDivider()
        Text(
            text = "Extend Credentials", modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(ExtendCredentials) })
        )
        BiDivider()
        Text(
            text = "Authenticate", modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(Authenticate) }
                )
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Questions or issues?",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Read through our developer docs for more details on our embedded SDK or reach out to support.",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "View Developer Docs",
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(VisitDocsEvent(Uri.parse("https://developer.beyondidentity.com"))) }
                )
        )
        BiDivider()
        Text(
            text = "Visit Support", modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(VisitSupportEvent(Uri.parse("https://beyondidentity.atlassian.net/wiki/spaces/CS/overview"))) }
                )
        )
    }
}

@Preview
@Composable
fun EmbeddedGetStartedPreview() {
    BiSdkAndroidTheme {
        EmbeddedGetStartedLayout(
            EmbeddedGetStartedState(),
            {},
            {},
            {},
            {},
            {}
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EmbeddedGetStartedPreviewDark() {
    BiSdkAndroidTheme {
        EmbeddedGetStartedLayout(
            EmbeddedGetStartedState(),
            {},
            {},
            {},
            {},
            {}
        )
    }
}
