package com.example.loanapplication.api

import com.example.loanapplication.api.response.ApiResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST("register")
    fun registerUser(@Body user: User): Call<ApiResponse<String>>

    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<ApiResponse<String>>

    @POST("verifyOtp")
    fun verifyOtp(@Body verificationRequest: VerificationRequest): Call<ApiResponse<String>>

    @POST("resendOtp")
    fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): Call<ApiResponse<String>>

    @GET("getUsername")
    fun getUsername(@Query("email") email: String): Call<ApiResponse<String>>

    @POST("passwordReset")
    fun initiatePasswordReset(@Body resetRequest: PasswordResetRequest): Call<ApiResponse<String>>

    @PATCH("verifyPasswordResetOtp")
    fun verifyPasswordResetOtp(@Body resetRequest: PasswordResetRequest):Call<ApiResponse<String>>
}