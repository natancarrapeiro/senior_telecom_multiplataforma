package com.example.senior_telecom_multiplataforma

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun openDialer(phoneNumber: String) {
    val context = LocalContext.current
    // Limpa o número: remove espaços, parênteses e traços
    val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$cleanNumber")
    }
    context.startActivity(intent)
}

actual fun makePhoneCall(phoneNumber: String) {
    // Para chamadas fora de composables, poderíamos usar um static context ou similar, 
    // mas para simplificar, a ação principal está no openDialer.
}
