package com.beyondidentity.authenticator.sdk.android.composeui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
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

fun buttonTestTag(testTag: String): String = "$testTag Button"
fun inputTestTag(testTag: String): String = "$testTag Input"
fun resultTestTag(testTag: String): String = "$testTag Result"

@Composable
@Preview(showBackground = true)
fun BiAppBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.BiAppBarColor,
    ) {
        Icon(
            painter = painterResource(id = drawable.ic_toolbar_bi_icon),
            contentDescription = null,
            tint = BiPrimaryMain,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun BiButton(title: String, testTag: String, onButtonClicked: () -> Unit) {
    OutlinedButton(
        onClick = onButtonClicked,
        modifier = Modifier.testTag(testTag),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.Gray),
        contentPadding = PaddingValues(start = 16.dp, top = 13.dp, bottom = 13.dp, end = 16.dp),
    ) {
        Row {
            Text(text = title, color = Color.Black, fontWeight = FontWeight.Light, fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(fill = true, weight = .5F))

            Image(
                painter = painterResource(id = drawable.outline_chevron_right_24),
                colorFilter = ColorFilter.tint(Color.Black),
                contentDescription = "",
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBiButton() {
    BiSdkAndroidTheme {
        BiButton(
            title = "title of user input",
            testTag = "TestTag",
            onButtonClicked = { },
        )
    }
}

@Composable
fun BiTextWithChevron(text: String, testTag: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() })
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .testTag(testTag),
    ) {
        Text(text = text)

        Spacer(modifier = Modifier.weight(fill = true, weight = .5F))

        Image(
            painter = painterResource(id = drawable.outline_chevron_right_24),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = "",
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBiTextWithChevron() {
    BiSdkAndroidTheme {
        BiTextWithChevron(
            text = "Text",
            testTag = "TestTag",
            onClick = { },
        )
    }
}

@Composable
@Preview(showBackground = true)
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
    testTag: String,
    onButtonClicked: () -> Unit,
    resultText: String,
    progressEnabled: Boolean,
) {
    Column(modifier = modifier) {
        Text(text = descriptionText)

        Button(
            onClick = onButtonClicked,
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .testTag(buttonTestTag(testTag)),
        ) {
            Text(text = buttonText)
        }

        ProgressIndicator(progressEnabled = progressEnabled)

        ResponseDataView(
            data = resultText,
            testTag = resultTestTag(testTag),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewInteractionResultView() {
    BiSdkAndroidTheme {
        InteractionResultView(
            descriptionText = "title of user input",
            buttonText = "submit text",
            testTag = "testTag",
            onButtonClicked = { },
            resultText = "result text",
            progressEnabled = true,
        )
    }
}

@Composable
fun ResponseDataView(
    modifier: Modifier = Modifier,
    data: String,
    testTag: String,
) {
    val scrollState = rememberScrollState()
    if (data.isNotEmpty()) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colors.BiGray300)
                .fillMaxWidth()
                .then(modifier),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(8.dp),
            ) {
                Text(
                    text = "Response Data",
                    style = MaterialTheme.typography.subtitle2,
                )

                SelectionContainer {
                    Text(
                        text = data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(testTag),
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun InteractionResponseInputView(
    modifier: Modifier = Modifier,
    description: String,
    inputValue: String,
    inputHint: String,
    onInputChanged: (String) -> Unit,
    buttonText: String,
    testTag: String,
    onSubmit: () -> Unit,
    submitResult: String,
    progressEnabled: Boolean,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Text(
            text = description,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag(inputTestTag(testTag)),
            value = inputValue,
            onValueChange = onInputChanged,
            label = {
                Text(
                    text = inputHint,
                    style = MaterialTheme.typography.body1,
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
                .fillMaxWidth()
                .padding(top = 8.dp)
                .testTag(buttonTestTag(testTag)),
        ) {
            Text(text = buttonText)
        }

        ProgressIndicator(progressEnabled = progressEnabled)

        ResponseDataView(
            data = submitResult,
            modifier = Modifier.padding(top = 16.dp),
            testTag = resultTestTag(testTag),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewInteractionResponseInputView() {
    BiSdkAndroidTheme {
        InteractionResponseInputView(
            description = "title of user input",
            inputValue = "user@email.com",
            inputHint = "Email address",
            onInputChanged = { },
            buttonText = "submit text",
            testTag = "testTag",
            onSubmit = { },
            submitResult = "result",
            progressEnabled = true,
        )
    }
}

@Composable
fun ResponseInputView(
    modifier: Modifier = Modifier,
    description: String,
    buttonText: String,
    testTag: String,
    onSubmit: () -> Unit,
    submitResult: String,
    progressEnabled: Boolean,
) {
    Column(modifier = modifier) {
        Text(
            text = description,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )

        Button(
            onClick = { onSubmit() },
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .testTag(buttonTestTag(testTag)),
        ) {
            Text(text = buttonText)
        }

        ProgressIndicator(progressEnabled = progressEnabled)

        ResponseDataView(
            data = submitResult,
            modifier = Modifier.padding(top = 16.dp),
            testTag = resultTestTag(testTag),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewResponseInputView() {
    BiSdkAndroidTheme {
        ResponseInputView(
            description = "title of user input",
            buttonText = "submit text",
            testTag = "testTag",
            onSubmit = { },
            submitResult = "result",
            progressEnabled = true,
        )
    }
}

@Composable
fun ProgressIndicator(
    progressEnabled: Boolean,
) {
    if (progressEnabled) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(1.dp)
                    .size(24.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewProgressIndicator() {
    BiSdkAndroidTheme {
        ProgressIndicator(
            progressEnabled = true,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Spacer16() {
    BiSdkAndroidTheme {
        Spacer(
            Modifier
                .height(16.dp)
                .width(16.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Spacer32() {
    BiSdkAndroidTheme {
        Spacer(
            Modifier
                .height(32.dp)
                .width(32.dp),
        )
    }
}
