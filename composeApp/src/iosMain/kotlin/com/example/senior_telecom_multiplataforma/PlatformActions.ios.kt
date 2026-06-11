package com.example.senior_telecom_multiplataforma

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.Foundation.NSURL

@Composable
actual fun openDialer(phoneNumber: String) {
    makePhoneCall(phoneNumber)
}

actual fun makePhoneCall(phoneNumber: String) {
    // Limpa o número para o iOS também
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val url = NSURL(string = "tel:$cleanNumber")
    if (UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}
