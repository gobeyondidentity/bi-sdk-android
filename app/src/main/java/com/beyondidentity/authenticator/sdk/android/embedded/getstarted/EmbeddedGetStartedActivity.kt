package com.beyondidentity.authenticator.sdk.android.embedded.getstarted

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.BindCredentialEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.ManageCredentials
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.UrlValidation
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitDocsEvent
import com.beyondidentity.authenticator.sdk.android.embedded.getstarted.EmbeddedGetStartedEvents.VisitSupportEvent
import com.beyondidentity.authenticator.sdk.android.embedded.managecredentials.ManageCredentialsActivity
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
                        EmbeddedGetStartedScreen(this@EmbeddedGetStartedActivity, viewModel)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    ManageCredentials -> launchActivity(event = event)
                    Authenticate -> launchActivity(event = event)
                    UrlValidation -> launchActivity(event = event)
                    is BindCredentialEvent -> bindCredentialMessage(event.result)
                    is VisitDocsEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                    is VisitSupportEvent -> startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                }
            }
        }
    }

    private fun bindCredentialMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun launchActivity(event: EmbeddedGetStartedEvents) {
        when (event) {
            ManageCredentials -> startActivity(Intent(this, ManageCredentialsActivity::class.java))
            Authenticate -> startActivity(Intent(this, EmbeddedAuthenticateActivity::class.java))
            UrlValidation -> startActivity(Intent(this, EmbeddedUrlValidationActivity::class.java))
            else -> Timber.d("noop")
        }
    }
}

@Composable
fun EmbeddedGetStartedScreen(activity: FragmentActivity, viewModel: EmbeddedGetStartedViewModel) {
    EmbeddedGetStartedLayout(
        state = viewModel.state,
        onRegisterCredentialUsernameTextChange = viewModel::onCredentialBindingLinkUsernameTextChange,
        onRegisterCredential = { viewModel.onRegisterCredential(viewModel.state.registerUsername) },
        onRecoverCredentialUsernameTextChange = viewModel::onRecoverCredentialBindingLinkUsernameTextChange,
        onRecoverCredential = { viewModel.onRecoverCredential(viewModel.state.recoverUsername) },
        onBindCredentialUrlTextChange = viewModel::onBindCredentialUrlTextChange,
        onBindCredential = {
            if (viewModel.state.bindCredentialUrl.isEmpty()) {
                Toast.makeText(activity, "Please provide a Bind Credential URL", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.onBindCredential(viewModel.state.bindCredentialUrl)
            }
        },
        onNavigate = viewModel::onGetStartedEvent,
    )
}

@Composable
fun EmbeddedGetStartedLayout(
    state: EmbeddedGetStartedState,
    onRegisterCredentialUsernameTextChange: (String) -> Unit,
    onRegisterCredential: () -> Unit,
    onRecoverCredentialUsernameTextChange: (String) -> Unit,
    onRecoverCredential: () -> Unit,
    onBindCredentialUrlTextChange: (String) -> Unit,
    onBindCredential: () -> Unit,
    onNavigate: (EmbeddedGetStartedEvents) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
    ) {
        Text(
            text = "Embedded SDK",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
        )

        BiVersionText()

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        BindCredential1Layout(
            state,
            onRegisterCredentialUsernameTextChange,
            onRegisterCredential,
            onRecoverCredentialUsernameTextChange,
            onRecoverCredential,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        BindCredential2Layout(
            state,
            onBindCredentialUrlTextChange,
            onBindCredential,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "SDK Functionality",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        Text(
            text = "Explore the various functions available when a Credential exists on the device.",
            modifier = Modifier.padding(bottom = 16.dp),
        )

        BiTextWithChevron(
            text = "Manage Credentials",
            testTag = "Manage Credentials",
            onClick = { onNavigate(ManageCredentials) },
        )

        BiDivider()

        BiTextWithChevron(
            text = "Authenticate",
            testTag = "Authenticate",
            onClick = { onNavigate(Authenticate) },
        )

        BiDivider()

        BiTextWithChevron(
            text = "URL Validation",
            testTag = "URL Validation",
            onClick = { onNavigate(UrlValidation) },
        )

        BiDivider()

        Text(
            text = "Questions or issues?",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        Text(
            text = "Read through our developer docs for more details on our embedded SDK or reach out to support.",
            modifier = Modifier.padding(bottom = 16.dp),
        )

        BiTextWithChevron(
            text = "View Developer Docs",
            testTag = "View Developer Docs",
            onClick = { onNavigate(VisitDocsEvent(Uri.parse("https://developer.beyondidentity.com/docs/v1/sdks/kotlin-sdk/overview"))) },
        )

        BiDivider()

        BiTextWithChevron(
            text = "Visit Support",
            testTag = "Visit Support",
            onClick = { onNavigate(VisitSupportEvent(Uri.parse("https://beyondidentity.atlassian.net/wiki/spaces/CS/overview"))) },
        )
    }
}

@Composable
fun BindCredential1Layout(
    state: EmbeddedGetStartedState,
    onRegisterCredentialUsernameTextChange: (String) -> Unit,
    onRegisterCredential: () -> Unit,
    onRecoverCredentialUsernameTextChange: (String) -> Unit,
    onRecoverCredential: () -> Unit,
) {
    Text(
        text = "Get Started",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
    )

    Text(
        text = "Bind Credential".uppercase(),
        style = MaterialTheme.typography.subtitle2,
    )

    InteractionResponseInputView(
        description = "To get started with using our embedded SDK sample app, " +
                "enter any username to bind a credential to this device.",
        inputValue = state.registerUsername,
        inputHint = "Username",
        inputTestTag = "Register Credential Input",
        onInputChanged = onRegisterCredentialUsernameTextChange,
        buttonText = "Bind Credential",
        testTag = "Register Credential",
        onSubmit = onRegisterCredential,
        submitResult = state.registerResult,
    )

    Spacer32()

    Text(
        text = "Recover Credential".uppercase(),
        style = MaterialTheme.typography.subtitle2,
    )

    InteractionResponseInputView(
        description = "If you have an account with a credential you can’t access anymore, " +
                "enter your username to recover your account and bind a credential to this device.",
        inputValue = state.recoverUsername,
        inputHint = "Username",
        inputTestTag = "Recover Credential Input",
        onInputChanged = onRecoverCredentialUsernameTextChange,
        buttonText = "Recover Account",
        testTag = "Recover Credential",
        onSubmit = onRecoverCredential,
        submitResult = state.recoverResult,
    )

    Spacer16()
}

@Composable
fun BindCredential2Layout(
    state: EmbeddedGetStartedState,
    onBindCredentialUrlTextChange: (String) -> Unit,
    onBindCredential: () -> Unit,
) {
    Text(
        text = "Bind Credential",
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(top = 32.dp),
    )

    InteractionResponseInputView(
        description = "Paste the Bind Credential URL you received in your email or generated through the API in order to bind a credential.",
        inputValue = state.bindCredentialUrl,
        inputHint = "Bind Credential URL",
        inputTestTag = "Bind Credential URL Input",
        onInputChanged = onBindCredentialUrlTextChange,
        buttonText = "Bind Credential",
        testTag = "Bind Credential URL",
        onSubmit = onBindCredential,
        submitResult = state.bindCredentialResult,
    )
}

@Composable
@Preview(showBackground = true)
fun BindCredential1Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp),
        ) {
            BindCredential1Layout(
                EmbeddedGetStartedState(),
                {},
                {},
                {},
                {},
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BindCredential2Preview() {
    BiSdkAndroidTheme {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp),
        ) {
            BindCredential2Layout(
                EmbeddedGetStartedState(),
                {},
                {},
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
            {},
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
            {},
        )
    }
}
