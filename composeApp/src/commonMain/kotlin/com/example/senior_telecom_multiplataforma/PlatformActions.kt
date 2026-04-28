package com.example.senior_telecom_multiplataforma

import androidx.compose.runtime.Composable

@Composable
expect fun openDialer(phoneNumber: String)

// Interface para chamar ações nativas sem ser Composable
expect fun makePhoneCall(phoneNumber: String)
