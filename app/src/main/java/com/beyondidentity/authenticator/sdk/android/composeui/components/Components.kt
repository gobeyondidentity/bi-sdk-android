package com.beyondidentity.authenticator.sdk.android.composeui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beyondidentity.authenticator.sdk.android.BuildConfig
import com.beyondidentity.authenticator.sdk.android.R
import com.beyondidentity.authenticator.sdk.android.R.drawable
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiAppBarColor
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiGray300
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiPrimaryMain
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme

@Preview
@Composable
fun BiAppBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.BiAppBarColor
    ) {
        Icon(
            painter = painterResource(id = drawable.ic_toolbar_bi_icon),
            contentDescription = null,
            tint = BiPrimaryMain,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BiButton(title: String, onButtonClicked: () -> Unit) {
    OutlinedButton(
        onClick = onButtonClicked,
        contentPadding = PaddingValues(start = 16.dp, top = 13.dp, bottom = 13.dp, end = 16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row {
            Text(text = title, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(fill = true, weight = .5F))

            Image(
                painter = painterResource(id = drawable.outline_chevron_right_24),
                colorFilter = ColorFilter.tint(Color.Black),
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BiVersionText(modifier: Modifier = Modifier) {
    Text(
        text = "SDK Version: ${BuildConfig.BUILD_CONFIG_BI_SDK_VERSION}\nEnvironment: ${stringResource(id = R.string.dev_env)}",
        modifier = modifier,
        color = Color.Gray,
    )
}

@Composable
fun BiDivider(modifier: Modifier = Modifier) {
    Divider(thickness = 1.dp, color = Color.LightGray, modifier = modifier)
}

@Composable
fun InteractionResultView(
    modifier: Modifier = Modifier,
    descriptionText: String,
    buttonText: String,
    onButtonClicked: () -> Unit,
    resultText: String,
) {
    Column(modifier = modifier) {
        Text(text = descriptionText)

        Button(
            onClick = onButtonClicked,
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = buttonText)
        }

        ResponseDataView(data = resultText)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInteractionResultView() {
    BiSdkAndroidTheme {
        InteractionResultView(
            descriptionText = "title of user input",
            buttonText = "user@email.com",
            onButtonClicked = { },
            resultText = "result text",
        )
    }
}

@Composable
fun ResponseDataView(
    modifier: Modifier = Modifier,
    data: String,
) {
    val scrollState = rememberScrollState()
    if (data.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.BiGray300)
                .then(modifier),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Text(
                    text = "Response Data",
                    style = MaterialTheme.typography.subtitle2
                )

                SelectionContainer {
                    Text(
                        text = data,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InteractionResponseInputView(
    modifier: Modifier = Modifier,
    description: String,
    inputValue: String,
    inputHint: String = "Email address",
    onInputChanged: (String) -> Unit,
    buttonText: String,
    onSubmit: () -> Unit,
    submitResult: String,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Text(
            text = description,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = inputValue,
            onValueChange = onInputChanged,
            label = {
                Text(
                    text = inputHint,
                    style = MaterialTheme.typography.body1
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onSubmit()
                keyboardController?.hide()
            }),
        )

        Button(
            onClick = {
                onSubmit()
                keyboardController?.hide()
            },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = buttonText)
        }

        ResponseDataView(
            data = submitResult,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInteractionResponseInputView() {
    BiSdkAndroidTheme {
        InteractionResponseInputView(
            description = "title of user input",
            inputValue = "user@email.com",
            onInputChanged = { },
            buttonText = "submit text",
            onSubmit = { },
            submitResult = "result",
        )
    }
}
