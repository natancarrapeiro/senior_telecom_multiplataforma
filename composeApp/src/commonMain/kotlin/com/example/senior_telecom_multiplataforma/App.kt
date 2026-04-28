package com.example.senior_telecom_multiplataforma

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cores Estilo iOS
val IosBackgroundColor = Color(0xFFF2F2F7)
val IosBlue = Color(0xFF007AFF)
val SeniorBlue = Color(0xFF005691)

@Composable
fun App() {
    var showWebView by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = IosBackgroundColor // Fundo cinza padrão iOS
        ) {
            Box(modifier = Modifier.safeDrawingPadding()) {
                if (showWebView) {
                    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                        TextButton(
                            onClick = { showWebView = false },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(12.dp).padding(end = 4.dp))
                            Text("Voltar", color = IosBlue)
                        }
                        MyWebView(
                            url = "https://centralixc.seniortelecom.com.br/central_assinante_web/login",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    HomeScreen(onLoginClick = { showWebView = true })
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BANNER PREMIUM ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SeniorBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text("PROMOÇÃO", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Internet 500 Mega", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Text("Por apenas R$ 99,90", color = Color.White.copy(alpha = 0.9f))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- BOTÃO ÁREA LOGADA (Estilo Botão iOS) ---
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IosBlue)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Área do Assinante", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
        }

        Spacer(Modifier.height(32.dp))

        // --- SEÇÃO AGROPADA: SUPORTE ---
        SectionHeader("SUPORTE E AJUDA")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            SupportItem(
                icon = Icons.Default.Info,
                title = "Conexão Lenta?",
                description = "Veja como resetar o sinal."
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- SEÇÃO AGRUPADA: TELEFONES ---
        SectionHeader("CONTATOS ÚTEIS")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                PhoneItem(name = "Suporte Técnico", number = "0800 123 4567")
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = IosBackgroundColor, thickness = 1.dp)
                PhoneItem(name = "Financeiro", number = "0800 765 4321")
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = IosBackgroundColor, thickness = 1.dp)
                PhoneItem(name = "Vendas", number = "(11) 98888-8888")
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, bottom = 8.dp),
        fontSize = 13.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun SupportItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = IosBlue.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = IosBlue, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(description, fontSize = 13.sp, color = Color.Gray)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
    }
}

@Composable
fun PhoneItem(name: String, number: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { makePhoneCall(number) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFF4CAF50).copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontSize = 12.sp, color = Color.Gray)
            Text(number, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
    }
}
