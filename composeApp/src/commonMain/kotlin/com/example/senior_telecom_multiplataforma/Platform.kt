package com.example.senior_telecom_multiplataforma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform