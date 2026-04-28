package com.example.senior_telecom_multiplataforma

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun openDialer(phoneNumber: String) {
    // No Android, usamos o contexto para disparar a Intent de discagem
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

actual fun makePhoneCall(phoneNumber: String) {
    // Implementação caso precise chamar fora de um Composable
}
