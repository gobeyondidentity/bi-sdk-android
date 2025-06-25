package com.beyondidentity.authenticator.sdk.android.embedded.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewRenderProcess
import android.webkit.WebViewRenderProcessClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.beyondidentity.authenticator.sdk.android.composeui.components.BiAppBar
import com.beyondidentity.authenticator.sdk.android.composeui.theme.BiSdkAndroidTheme
import com.beyondidentity.authenticator.sdk.android.configs.Auth0Config
import com.beyondidentity.authenticator.sdk.android.configs.OktaConfig
import com.beyondidentity.authenticator.sdk.android.embedded.webview.EmbeddedWebViewEvents.WebViewSuccess
import com.beyondidentity.embedded.sdk.EmbeddedSdk
import kotlinx.coroutines.launch
import timber.log.Timber

class EmbeddedWebViewActivity : ComponentActivity() {
    private val viewModel: EmbeddedWebViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BiSdkAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(topBar = {
                        BiAppBar()
                    }) {
                        EmbeddedWebViewScreen(viewModel)
                    }
                }
            }
        }

        intent?.data?.let { uri ->
            viewModel.onUrlTextChange(uri.toString())
        }

        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is WebViewSuccess -> {
                        val intent = Intent()
                        intent.data = viewModel.state.result.toUri()
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun EmbeddedWebViewScreen(viewModel: EmbeddedWebViewViewModel) {
    EmbeddedWebViewLayout(
        state = viewModel.state,
        onOverrideUrlLoading = {
            viewModel.onResultTextChange(it.toString())
            viewModel.onOverrideUrlLoading()
        }
    )
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
fun EmbeddedWebViewLayout(preview: Boolean = false, state: EmbeddedWebViewState, onOverrideUrlLoading: (Uri) -> Unit) {
    val scroll = rememberScrollState(0)

    Column {
        Row {
            Text(
                modifier = Modifier
                    .horizontalScroll(scroll)
                    .padding(4.dp),
                text = state.url
            )
        }

        Row {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        if (!preview) {
                            settings.javaScriptEnabled = true

                            webChromeClient = object : WebChromeClient() {}
                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    Timber.d("shouldOverrideUrlLoading($view, $request) | URL = ${request?.url})")
                                    request?.url?.let { url ->
                                        return when {
                                            EmbeddedSdk.isAuthenticateUrl(url.toString()) -> {
                                                onOverrideUrlLoading(url)
                                                true
                                            }
                                            Auth0Config.isRedirectUri(url) -> {
                                                onOverrideUrlLoading(url)
                                                true
                                            }
                                            OktaConfig.isRedirectUri(url) -> {
                                                onOverrideUrlLoading(url)
                                                true
                                            }
                                            else -> {
                                                super.shouldOverrideUrlLoading(view, request)
                                            }
                                        }
                                    }
                                    return super.shouldOverrideUrlLoading(view, request)
                                }
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                webViewRenderProcessClient = object : WebViewRenderProcessClient() {
                                    override fun onRenderProcessResponsive(
                                        view: WebView,
                                        renderer: WebViewRenderProcess?
                                    ) {
                                        Timber.d("onRenderProcessResponsive($view, $renderer)")
                                    }

                                    override fun onRenderProcessUnresponsive(
                                        view: WebView,
                                        renderer: WebViewRenderProcess?
                                    ) {
                                        Timber.d("onRenderProcessUnresponsive($view, $renderer)")
                                    }
                                }
                            }
                        }

                        if (url.isNullOrEmpty()) {
                            Timber.d("loadUrl(${state.url}) | URL = ${state.url})")
                            loadUrl(state.url)
                        }
                    }
                },
                update = {
                    if (it.url.isNullOrEmpty()) {
                        Timber.d("loadUrl(${state.url}) | URL = ${state.url})")
                        it.loadUrl(state.url)
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
fun EmbeddedWebViewPreviewLight() {
    BiSdkAndroidTheme {
        EmbeddedWebViewLayout(
            preview = false,
            state = EmbeddedWebViewState(url = "https://www.beyondidentity.com/")
        ) {}
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun EmbeddedWebViewPreviewDark() {
    BiSdkAndroidTheme {
        EmbeddedWebViewLayout(
            preview = true,
            state = EmbeddedWebViewState(url = "https://www.beyondidentity.com/")
        ) {}
    }
}
