package com.beyondidentity.authenticator.sdk.android.embedded.regandrecover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beyondidentity.authenticator.sdk.android.composeui.components.InteractionResponseInputView
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme


class EmbeddedUIRegAndRecoverActivity : ComponentActivity() {
    private val viewModel: RegisterAndRecoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    RegAndRecoverScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RegAndRecoverScreen(
    viewModel: RegisterAndRecoverViewModel,
) {
    RegAndRecoveryLayout(
        state = viewModel.state,
        onRegistrationEmailTextChange = viewModel::onRegistrationEmailTextChange,
        onRegisterUser = viewModel::onRegisterUser,
        onRecoveryEmailTextChange = viewModel::onRecoverEmailTextChange,
        onRecoverUser = viewModel::onRecoverUser,
    )
}

@Composable
fun RegAndRecoveryLayout(
    state: RegAndRecoverState,
    onRegistrationEmailTextChange: (String) -> Unit,
    onRegisterUser: () -> Unit,
    onRecoveryEmailTextChange: (String) -> Unit,
    onRecoverUser: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registration and Recovery",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "As a developer implementing the Embedded UI SDK, " +
                    "you have full control of the registration and recovery UI/UX. " +
                    "This is for demo purposes",
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        InteractionResponseInputView(
            description = "To register a new user and get a registration email link",
            inputValue = state.registerEmail,
            onInputChanged = onRegistrationEmailTextChange,
            buttonText = "Register User",
            onSubmit = onRegisterUser,
            submitResult = state.registerResult,
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        InteractionResponseInputView(
            description = "To recover an existing user and get a recovery email link",
            inputValue = state.recoverEmail,
            onInputChanged = onRecoveryEmailTextChange,
            buttonText = "Recover User",
            onSubmit = onRecoverUser,
            submitResult = state.recoverResult,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BiSdkAndroidTheme {
        RegAndRecoveryLayout(
            RegAndRecoverState(),
            {},
            {},
            {},
            {},
        )
    }
}
