package com.example.senior_telecom_multiplataforma

import androidx.compose.ui.graphics.Color
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Promotion(val title: String, val price: String, val color: String)

object RemoteConfigManager {
    // Acessa o Firebase de forma segura apenas quando necessário
    private val firebaseRemoteConfig get() = Firebase.remoteConfig

    private val _promotions = MutableStateFlow<List<Promotion>>(
        listOf(Promotion("Internet 500 Mega", "R$ 99,90", "#005691"))
    )
    val promotions: StateFlow<List<Promotion>> = _promotions

    suspend fun fetchAndActivate() {
        try {
            // Tenta buscar do Firebase. Se o arquivo google-services.json estiver faltando, 
            // ele vai cair no catch e manter os dados padrão acima.
            firebaseRemoteConfig.fetchAndActivate()
            val jsonString = firebaseRemoteConfig.getValue("promotions_json").asString()
            if (jsonString.isNotEmpty()) {
                val decoded = Json.decodeFromString<List<Promotion>>(jsonString)
                _promotions.value = decoded
            }
        } catch (e: Exception) {
            println("Firebase não inicializado ou erro no Remote Config: ${e.message}")
            // Mantém os planos padrão para o app não travar
        }
    }

    fun String.toColor(): Color {
        return try {
            val hex = this.removePrefix("#")
            val colorInt = hex.toLong(16)
            if (hex.length == 6) {
                Color(colorInt or 0xFF000000)
            } else {
                Color(colorInt)
            }
        } catch (e: Exception) {
            Color(0xFF005691)
        }
    }
}
