package com.example.senior_telecom_multiplataforma

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import platform.WebKit.WKWebView
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.CoreGraphics.CGRectMake
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MyWebView(url: String, modifier: Modifier) {
    UIKitView(
        factory = {
            // Criamos o WebView sem passar o frame complexo,
            // o Compose cuidará do tamanho através do modifier.
            val webView = WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
            webView
        },
        modifier = modifier,
        update = { webView ->
            val nsUrl = NSURL(string = url)
            webView.loadRequest(NSURLRequest(nsUrl))
        }
    )
}