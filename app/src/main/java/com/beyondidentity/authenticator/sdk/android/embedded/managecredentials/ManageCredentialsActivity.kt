package com.beyondidentity.authenticator.sdk.android.embedded.managecredentials

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
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResultView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme

class ManageCredentialsActivity : ComponentActivity() {
    private val viewModel: ManageCredentialsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        ManageCredentialsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ManageCredentialsScreen(viewModel: ManageCredentialsViewModel) {
    ManageCredentialsLayout(
        viewModel.state.getCredentialResult,
        viewModel::onGetCredentials,
        viewModel.state.deleteCredential,
        viewModel::onDeleteCredentialTextChange,
        viewModel.state.deleteCredentialResult,
        viewModel::onDeleteCredentials,
    )
}

@Composable
fun ManageCredentialsLayout(
    getCredentialResult: String,
    onGetCredential: () -> Unit,
    deleteCredential: String,
    onDeleteCredentialTextChange: (String) -> Unit,
    deleteCredentialResult: String,
    onDeleteCredential: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp),
    ) {
        Text(
            text = "Credential Management",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
        )

        BiVersionText()

        Text(
            text = "View Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        InteractionResultView(
            descriptionText = "View details of your Credential, such as date created, " +
                    "identity and other information related to your device.",
            buttonText = "View Credential",
            testTag = "View Credential",
            onButtonClicked = onGetCredential,
            resultText = getCredentialResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Delete Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        InteractionResponseInputView(
            description = "Delete your credential on your device.",
            inputValue = deleteCredential,
            inputHint = "Credential ID",
            inputTestTag = "Delete Credential Input",
            onInputChanged = onDeleteCredentialTextChange,
            buttonText = "Delete Credential",
            testTag = "Delete Credential",
            onSubmit = onDeleteCredential,
            submitResult = deleteCredentialResult,
        )
    }
}
