package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.AuthenticateEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindCredentialEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.CredentialRegistration
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.ManageCredentials
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitDocsEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitSupportEvent
import com.beyondidentity.authenticator.sdk.android.embedded.managecred.ManageCredentialsActivity
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
                        EmbeddedGetStartedScreen(this@EmbeddedGetStartedActivity, viewModel)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    ManageCredentials -> launchActivity(event = event)
                    is VisitDocsEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    is VisitSupportEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    is BindCredentialEvent -> bindCredentialMessage(event.result)
                    is AuthenticateEvent -> authenticateMessage(event.result)
                    is CredentialRegistration -> credentialRegistrationMessage(event.result)
                }
            }
        }
    }

    private fun bindCredentialMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun authenticateMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun credentialRegistrationMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun launchActivity(event: EmbeddedGetStartedEvents) {
        when (event) {
            ManageCredentials -> startActivity(Intent(this, ManageCredentialsActivity::class.java))
            else -> Timber.d("noop")
        }
    }
}

@Composable
fun EmbeddedGetStartedScreen(activity: FragmentActivity, viewModel: EmbeddedGetStartedViewModel) {
    EmbeddedGetStartedLayout(
        state = viewModel.state,
        onBindCredentialUrlTextChange = viewModel::onBindCredentialUrlTextChange,
        onBindCredential = { viewModel.onBindCredential(viewModel.state.bindCredentialUrl) },
        onAuthenticateUrlTextChange = viewModel::onAuthenticateUrlTextChange,
        onAuthenticate = {
            viewModel.onAuthenticate(activity, viewModel.state.authenticateUrl)
        },
        onUrlValidationBindCredentialUrlTextChange = viewModel::onUrlValidationBindCredentialUrlTextChange,
        onValidateBindCredentialUrl = viewModel::onValidateBindCredentialUrl,
        onUrlValidationAuthenticateUrlTextChange = viewModel::onUrlValidationAuthenticateUrlTextChange,
        onValidateAuthenticateUrl = viewModel::onValidateAuthenticateUrl,
        onNavigate = viewModel::onGetStartedEvent,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmbeddedGetStartedLayout(
    state: EmbeddedGetStartedState,
    onBindCredentialUrlTextChange: (String) -> Unit,
    onBindCredential: () -> Unit,
    onAuthenticateUrlTextChange: (String) -> Unit,
    onAuthenticate: () -> Unit,
    onUrlValidationBindCredentialUrlTextChange: (String) -> Unit,
    onValidateBindCredentialUrl: () -> Unit,
    onUrlValidationAuthenticateUrlTextChange: (String) -> Unit,
    onValidateAuthenticateUrl: () -> Unit,
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
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        BiVersionText()

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Bind Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp)
        )

        InteractionResponseInputView(
            description = "Paste the Bind Credential URL you received in your email or generated through the API in order to bind a credential.",
            inputValue = state.bindCredentialUrl,
            inputHint = "Bind Credential URL",
            onInputChanged = onBindCredentialUrlTextChange,
            buttonText = "Bind Credential",
            onSubmit = onBindCredential,
            submitResult = state.bindCredentialResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Authenticate",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp)
        )

        InteractionResponseInputView(
            description = "Authenticates against a credential bound to the device. If more than one credential is present, you must select a credential during authentication.",
            inputValue = state.authenticateUrl,
            inputHint = "Authenticate URL",
            onInputChanged = onAuthenticateUrlTextChange,
            buttonText = "Authenticate",
            onSubmit = onAuthenticate,
            submitResult = state.authenticateResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Url Validation",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp)
        )

        InteractionResponseInputView(
            description = "Paste a Url here to validate if it's a bind credential url.",
            inputValue = state.urlValidationBindCredentialUrl,
            inputHint = "Bind Credential URL",
            onInputChanged = onUrlValidationBindCredentialUrlTextChange,
            buttonText = "Validate Url",
            onSubmit = onValidateBindCredentialUrl,
            submitResult = state.validateBindCredentialUrlResult,
        )

        InteractionResponseInputView(
            description = "Paste a Url here to validate if it's an authenticate url.",
            inputValue = state.urlValidationAuthenticateUrl,
            inputHint = "Authenticate URL",
            onInputChanged = onUrlValidationAuthenticateUrlTextChange,
            buttonText = "Validate Url",
            onSubmit = onValidateAuthenticateUrl,
            submitResult = state.validateAuthenticateUrlResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Credentials",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 32.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigate(ManageCredentials) }
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
            text = "Visit Support",
            modifier = Modifier
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
            {},
            {},
            {},
            {},
            {},
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
            {},
            {},
            {},
            {},
            {},
        )
    }
}
