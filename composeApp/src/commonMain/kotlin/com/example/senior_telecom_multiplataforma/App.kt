package com.example.senior_telecom_multiplataforma

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App() {
    MaterialTheme {
        // O Surface garante o fundo branco e o safeDrawingPadding
        // evita que o topo do site fique escondido atrás do relógio/bateria
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MyWebView(
                    url = "https://centralixc.seniortelecom.com.br/central_assinante_web/login",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}