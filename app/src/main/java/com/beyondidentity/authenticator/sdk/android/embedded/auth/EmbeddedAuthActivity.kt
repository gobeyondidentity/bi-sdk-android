package com.beyondidentity.authenticator.sdk.android.embedded.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResultView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import java.util.*

class EmbeddedAuthActivity : ComponentActivity() {
    private val viewModel: EmbeddedAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        EmbeddedAuthScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun EmbeddedAuthScreen(viewModel: EmbeddedAuthViewModel) {
    EmbeddedAuthLayout(
        viewModel.state.authenticationResult,
        viewModel::onAuthenticate,
        viewModel.state.pkceResult,
        viewModel::onGeneratePkce,
        viewModel.state.authorizeResult,
        viewModel::onAuthorize,
        viewModel.state.authorizeExchangeResult,
        viewModel::onAuthorizeExchange,
    )
}

@Composable
fun EmbeddedAuthLayout(
    authenticationResult: String,
    onAuthenticate: () -> Unit,
    pkceResult: String,
    onGeneratePkce: () -> Unit,
    authorizationResult: String,
    onAuthorize: () -> Unit,
    authorizeExchangeResult: String,
    onAuthorizeExchange: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Authenticate",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        BiVersionText()

        Text(
            text = "After authentication, you’ll receive and access and ID token, which will be used " +
                    "to get information on the user and authenticate on APIs. The flow of getting tokens " +
                    "depends on your OIDC configuration, and there are two types - Public (without client secret) " +
                    "and Confidential (with client secret).",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        Text(
            text = "OIDC Public Client",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        InteractionResultView(
            descriptionText = "Public clients are unable to keep the secret secure, i.e. " +
                    "front-end app with no back-end. Use the following “Authenticate” " +
                    "function for a public client, which will go through the whole flow to get the Access and ID tokens.",
            buttonText = "Authenticate",
            onButtonClicked = onAuthenticate,
            resultText = authenticationResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "OIDC Confidential Client",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        Text(
            text = "Confidential clients are able to keep the secret secure, i.e. your backend.",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        Text(
            text = "Step 1: PKCE Challenge (Optional)".uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        InteractionResultView(
            descriptionText = "Use PKCE for increased security. " +
                    "If PKCE is used to start the flow, it needs to be used to complete it. " +
                    "Read more in our developer docs.",
            buttonText = "Generate PKCE Challenge",
            onButtonClicked = onGeneratePkce,
            resultText = pkceResult,
        )

        Text(
            text = "Step 2: Authorize".uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        InteractionResultView(
            descriptionText = "Use the “authorize” function for a confidential client. " +
                    "You will get an authorization code that needs to be exchanged for the Access and ID token.",
            buttonText = "Authorize",
            onButtonClicked = onAuthorize,
            resultText = authorizationResult,
        )

        Text(
            text = "Step 3: Exchange Authorization Code for ID and Access Token".uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        InteractionResultView(
            descriptionText = "We need the client secret in order to exchange the authorization code for Access and ID tokens." +
                    "\n\nIMPORTANT NOTE: The client secret is stored on the device for demo purposes only. This should not be done in production.",
            buttonText = "Exchange code for tokens",
            onButtonClicked = onAuthorizeExchange,
            resultText = authorizeExchangeResult,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}