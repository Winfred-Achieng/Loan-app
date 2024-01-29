package com.example.loanapplication.api.response

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val id: Long?,
    val data: T?
)
