package com.example.loanapplication.api

data class LoginRequest(
    val email: String?,
    val phone: String?,
    val password: String
)
