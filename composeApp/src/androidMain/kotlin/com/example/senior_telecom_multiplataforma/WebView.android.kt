package com.example.senior_telecom_multiplataforma

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun MyWebView(url: String, modifier: Modifier) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    
    // Usamos um estado simples para o refresh
    var isRefreshing by remember { mutableStateOf(false) }

    BackHandler(enabled = webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // Criamos o SwipeRefreshLayout programaticamente
                val swipeRefresh = androidx.swiperefreshlayout.widget.SwipeRefreshLayout(context)
                val webView = WebView(context)
                
                webViewInstance = webView

                webView.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            if (!isRefreshing) isLoading = true
                            errorMessage = null
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            isRefreshing = false
                            swipeRefresh.isRefreshing = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            if (request?.isForMainFrame == true) {
                                errorMessage = "Ocorreu um erro ao carregar a página."
                                isLoading = false
                                isRefreshing = false
                                swipeRefresh.isRefreshing = false
                            }
                        }
                    }
                    loadUrl(url)
                }

                swipeRefresh.apply {
                    addView(webView)
                    setOnRefreshListener {
                        isRefreshing = true
                        webView.reload()
                    }
                }
                swipeRefresh
            },
            update = { swipeRefresh ->
                // O update garante que o WebView reflita mudanças de URL se necessário
            }
        )

        if (isLoading && !isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        errorMessage?.let { msg ->
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = msg)
                Button(onClick = {
                    errorMessage = null
                    webViewInstance?.reload()
                }) {
                    Text("Tentar Novamente")
                }
            }
        }
    }
}
