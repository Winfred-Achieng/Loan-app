package com.example.loanapplication.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("/register")
    fun registerUser(@Body user: User): Call<String>

    @POST("/login")
    fun loginUser(@Body user: User): Call<String>


}