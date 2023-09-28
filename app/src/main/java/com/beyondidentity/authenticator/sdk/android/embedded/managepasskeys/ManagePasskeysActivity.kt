package com.beyondidentity.authenticator.sdk.android.embedded.managepasskeys

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResultView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme

class ManagePasskeysActivity : ComponentActivity() {
    private val viewModel: ManagePasskeysViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        ManagePasskeysScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ManagePasskeysScreen(viewModel: ManagePasskeysViewModel) {
    ManagePasskeysLayout(
        viewModel.state.getPasskeyResult,
        viewModel.state.getPasskeyProgress,
        viewModel::onGetPasskeys,
        viewModel.state.deletePasskey,
        viewModel::onDeletePasskeyTextChange,
        viewModel.state.deletePasskeyResult,
        viewModel.state.deletePasskeyProgress,
        viewModel::onDeletePasskeys,
    )
}

@Composable
fun ManagePasskeysLayout(
    getPasskeyResult: String,
    getPasskeyProgress: Boolean,
    onGetPasskey: () -> Unit,
    deletePasskey: String,
    onDeletePasskeyTextChange: (String) -> Unit,
    deletePasskeyResult: String,
    deletePasskeyProgress: Boolean,
    onDeletePasskey: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp),
    ) {
        Text(
            text = "Passkey Management",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .testTag("Passkey Management Header"),
        )

        BiVersionText()

        Text(
            text = "View Passkey",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        InteractionResultView(
            descriptionText = "View details of your passkey, such as date created, " +
                    "identity and other information related to your device.",
            buttonText = "View Passkey",
            testTag = "View Passkey",
            onButtonClicked = onGetPasskey,
            resultText = getPasskeyResult,
            progressEnabled = getPasskeyProgress,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Delete Passkey",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
        )

        InteractionResponseInputView(
            description = "Delete your passkey on your device.",
            inputValue = deletePasskey,
            inputHint = "Passkey ID",
            onInputChanged = onDeletePasskeyTextChange,
            buttonText = "Delete Passkey",
            testTag = "Delete Passkey",
            onSubmit = onDeletePasskey,
            submitResult = deletePasskeyResult,
            progressEnabled = deletePasskeyProgress,
        )
    }
}
