package com.example.senior_telecom_multiplataforma

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import platform.WebKit.WKWebView
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKNavigation
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIRefreshControl
import platform.UIKit.UIControlEventValueChanged
import platform.darwin.NSObject
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MyWebView(url: String, modifier: Modifier) {
    UIKitView(
        factory = {
            val webView = WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
            
            // Adicionando Pull-to-Refresh nativo do iOS
            val refreshControl = UIRefreshControl()
            val dispatcher = object : NSObject() {
                @ObjCAction
                fun onRefresh() {
                    webView.reload()
                }
            }
            
            refreshControl.addTarget(
                target = dispatcher,
                action = platform.Foundation.NSSelectorFromString("onRefresh"),
                forControlEvents = UIControlEventValueChanged
            )
            
            webView.scrollView.refreshControl = refreshControl
            
            // Delegate para parar a animação de refresh quando terminar de carregar
            webView.navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {
                override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                    webView.scrollView.refreshControl?.endRefreshing()
                }
                
                override fun webView(webView: WKWebView, didFailNavigation: WKNavigation?, withError: platform.Foundation.NSError) {
                    webView.scrollView.refreshControl?.endRefreshing()
                }
            }
            
            webView
        },
        modifier = modifier,
        update = { webView ->
            if (webView.URL == null) {
                val nsUrl = NSURL(string = url)
                webView.loadRequest(NSURLRequest(nsUrl))
            }
        }
    )
}
