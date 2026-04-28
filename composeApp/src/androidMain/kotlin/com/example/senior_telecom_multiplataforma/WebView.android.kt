package com.example.senior_telecom_multiplataforma

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun MyWebView(url: String, modifier: Modifier) {
    // Estados para controlar a UI reativa
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // Gerenciador do Botão Voltar (Igual ao seu OnBackPressed nativo)
    // Se o site puder voltar, ele volta. Se não, o app fecha.
    BackHandler(enabled = webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewInstance = this

                    // FORÇA O WEBVIEW A PREENCHER TODO O ESPAÇO (Resolve o bug da tela branca)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // --- SUAS CONFIGURAÇÕES NATIVAS ---
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true // Essencial para o portal de login IXC
                        databaseEnabled = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        builtInZoomControls = true
                        displayZoomControls = false
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            errorMessage = null
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            // Trata apenas erros no frame principal (carregamento da página)
                            if (request?.isForMainFrame == true) {
                                val errorCode = error?.errorCode
                                errorMessage = when (errorCode) {
                                    -2, -6, -8 -> "Ops! O sistema está fora do ar.\nTente novamente mais tarde."
                                    -10 -> "Sem conexão com a internet.\nVerifique seu Wi-Fi ou dados móveis."
                                    else -> "Ocorreu um erro ao carregar a página."
                                }
                                isLoading = false
                            }
                        }
                    }
                    loadUrl(url)
                }
            },
            update = { webView ->
                // Evita recarregar a página se a URL for a mesma ou se houver erro
                if (webView.url != url && errorMessage == null) {
                    webView.loadUrl(url)
                }
            }
        )

        // --- UI DE CARREGAMENTO (CircularProgressIndicator) ---
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // --- UI DE ERRO (Centralizada na tela) ---
        errorMessage?.let { msg ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = msg)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    errorMessage = null
                    webViewInstance?.loadUrl(url)
                }) {
                    Text("Tentar Novamente")
                }
            }
        }
    }
}