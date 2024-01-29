package com.example.loanapplication.api

data class VerificationRequest(
    val enteredOtp: String,
    val email: String
)
