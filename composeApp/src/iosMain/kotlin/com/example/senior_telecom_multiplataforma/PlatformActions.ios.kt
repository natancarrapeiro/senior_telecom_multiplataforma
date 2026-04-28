package com.example.senior_telecom_multiplataforma

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.Foundation.NSURL

@Composable
actual fun openDialer(phoneNumber: String) {
    makePhoneCall(phoneNumber)
}

actual fun makePhoneCall(phoneNumber: String) {
    val url = NSURL(string = "tel:$phoneNumber")
    if (UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}
