package com.beyondidentity.authenticator.sdk.android.embedded.extend

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.App
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiDivider
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiVersionText
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResultView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import com.beyondidentity.embedded.sdk.EmbeddedSdk

class ExtendCredentialActivity : ComponentActivity() {
    private val viewModel: ExtendCredentialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        ExtendCredentialScreen(viewModel)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        EmbeddedSdk.cancelExtendCredentials { }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == App.EMBEDDED_KEYGUARD_REQUEST) {
            when (resultCode) {
                RESULT_OK -> EmbeddedSdk.answer(true)
                RESULT_CANCELED -> EmbeddedSdk.answer(false)
            }
        }
    }
}

@Composable
fun ExtendCredentialScreen(viewModel: ExtendCredentialViewModel) {
    ExtendCredentialLayout(
        extendToken = viewModel.state.generatedExtendToken,
        onStartExtendCredential = viewModel::onCredentialExtend,
        cancelResult = viewModel.state.cancelExtendResult,
        onCancelExtendCredential = viewModel::onCancelExtendCredential,
        registerTokenInputValue = viewModel.state.registerTokenInputValue,
        onRegisterTokenInputValueChanged = viewModel::onRegisterTokenInputValueChanged,
        onRegisterTokenSubmit = viewModel::onRegisterCredentialWithToken,
        registerCredentialWithTokenResult = viewModel.state.registerCredentialWithTokenResult,
    )
}

@Composable
fun ExtendCredentialLayout(
    extendToken: String,
    onStartExtendCredential: () -> Unit,
    cancelResult: String,
    onCancelExtendCredential: () -> Unit,
    registerTokenInputValue: String,
    onRegisterTokenInputValueChanged: (String) -> Unit,
    onRegisterTokenSubmit: () -> Unit,
    registerCredentialWithTokenResult: String,
) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Register/Extend Credential",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
        )

        BiVersionText()

        Text(
            text = "Extend Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        InteractionResultView(
            descriptionText = "Click “extend credential” to generate a 9-digit export " +
                    "token that’ll be used to extend your credential to another device. " +
                    "\n\nNote: Lock screen needs to be set on the device",
            buttonText = "Extend Credential",
            onButtonClicked = onStartExtendCredential,
            resultText = extendToken,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Cancel Extend Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        InteractionResultView(
            descriptionText = "The “Extend credential” action blocks the embedded SDK from " +
                    "performing other actions. The export needs to finish or be explicitly cancelled.",
            buttonText = "Cancel Extend",
            onButtonClicked = onCancelExtendCredential,
            resultText = cancelResult,
        )

        BiDivider(modifier = Modifier.padding(top = 32.dp))

        Text(
            text = "Register Credential",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 32.dp)
        )

        InteractionResponseInputView(
            description = "To import a credential from another device, enter the " +
                    "export token generated on your device with a credential.",
            inputValue = registerTokenInputValue.uppercase(),
            inputHint = "Extend Credential Token",
            onInputChanged = {
                onRegisterTokenInputValueChanged(it)
            },
            buttonText = "Register Credential",
            onSubmit = onRegisterTokenSubmit,
            submitResult = registerCredentialWithTokenResult,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExtendCredentialLayoutPreview() {
    BiSdkAndroidTheme {
        ExtendCredentialLayout(
            "tko-fdf-fdf",
            {},
            "cancel result",
            {},
            "tod-34d-vff",
            {},
            {},
            "register with token result",
        )
    }
}