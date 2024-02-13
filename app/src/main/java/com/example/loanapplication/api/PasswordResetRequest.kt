package com.example.loanapplication.api

data class PasswordResetRequest(
    val email: String,
    val passwordOtp: String,
    val newPassword: String
)
