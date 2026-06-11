package com.example.senior_telecom_multiplataforma

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
import kotlinx.coroutines.delay
import com.example.senior_telecom_multiplataforma.RemoteConfigManager.toColor

// Cores Estilo iOS
val IosBackgroundColor = Color(0xFFF2F2F7)
val IosBlue = Color(0xFF007AFF)
val SeniorBlue = Color(0xFF005691)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var currentScreen by remember { mutableStateOf("home") }

    // Inicializa o Remote Config ao abrir o app
    LaunchedEffect(Unit) {
        RemoteConfigManager.fetchAndActivate()
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when(currentScreen) {
                                "webview" -> "Central do Assinante"
                                "support" -> "Autoatendimento"
                                else -> "Senior Telecom"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        if (currentScreen != "home") {
                            TextButton(onClick = { currentScreen = "home" }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBackIos,
                                        contentDescription = "Voltar",
                                        tint = IosBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Voltar", color = IosBlue, fontSize = 17.sp)
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = IosBackgroundColor,
                        titleContentColor = Color.Black
                    )
                )
            },
            containerColor = IosBackgroundColor
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (currentScreen) {
                    "webview" -> MyWebView(
                        url = "https://centralixc.seniortelecom.com.br/central_assinante_web/login",
                        modifier = Modifier.fillMaxSize().background(Color.White)
                    )
                    "support" -> SupportDetailScreen()
                    else -> HomeScreen(
                        onLoginClick = { currentScreen = "webview" },
                        onSupportClick = { currentScreen = "support" }
                    )
                }
            }
        }
    }
}

@Composable
fun PromotionBannerCarousel() {
    val promotions by RemoteConfigManager.promotions.collectAsState()
    val pagerState = rememberPagerState(pageCount = { promotions.size })

    // Auto-scroll a cada 4 segundos
    LaunchedEffect(promotions.size) {
        if (promotions.size > 1) {
            while (true) {
                delay(4000)
                val nextPage = (pagerState.currentPage + 1) % promotions.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth().height(160.dp)
    ) { pageIndex ->
        val promo = promotions[pageIndex]
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = promo.color.toColor()),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text("PROMOÇÃO", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(promo.title, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Text("Por apenas ${promo.price}", color = Color.White.copy(alpha = 0.9f))
                }
                
                // Indicador de página (bolinhas)
                if (promotions.size > 1) {
                    Row(
                        modifier = Modifier.align(Alignment.TopEnd),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        promotions.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = if (pagerState.currentPage == index) Color.White else Color.White.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onLoginClick: () -> Unit, onSupportClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BANNER INTERATIVO COM REMOTE CONFIG ---
        PromotionBannerCarousel()

        Spacer(Modifier.height(24.dp))

        // --- BOTÃO ÁREA LOGADA ---
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

        // --- SEÇÃO SUPORTE ---
        SectionHeader("SUPORTE E AJUDA")
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onSupportClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            SupportItem(
                icon = Icons.Default.Info,
                title = "Conexão Lenta ou Caindo?",
                description = "Veja como resolver agora."
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- SEÇÃO TELEFONES ---
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
fun SupportDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SectionHeader("1. SEM CONEXÃO")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TipItem("Verifique se os aparelhos estão ligados na tomada.")
                TipItem("Verifique se há alguma luz vermelha nos aparelhos (Modem/Roteador).")
                TipItem("Confirme se o sinal do Wi-Fi aparece na tela do seu celular.")
                TipItem("DICA MESTRA: Retire os aparelhos da tomada, aguarde 1 minuto e ligue-os novamente.")
            }
        }

        Spacer(Modifier.height(32.dp))

        SectionHeader("2. CONEXÃO LENTA")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TipItem("Verifique se a lentidão ocorre em todos os aparelhos (TV, Celular, PC).")
                TipItem("Confirme em qual rede está conectado (Redes 5G são mais rápidas que 2G).")
                TipItem("Se estiver no PC ou TV, prefira usar o cabo de rede para mais estabilidade.")
                TipItem("Verifique a distância entre você e o roteador (paredes diminuem o sinal).")
            }
        }
        
        Spacer(Modifier.height(32.dp))
        
        Text(
            "Se o problema persistir, entre em contato com nosso suporte técnico pelos telefones na tela inicial.",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun TipItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("•", color = IosBlue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        Text(text, fontSize = 15.sp, color = Color.DarkGray)
    }
}

@Composable
fun PhoneItem(name: String, number: String) {
    var triggerCall by remember { mutableStateOf(false) }
    
    if (triggerCall) {
        openDialer(number)
        SideEffect { triggerCall = false }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { triggerCall = true }
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
