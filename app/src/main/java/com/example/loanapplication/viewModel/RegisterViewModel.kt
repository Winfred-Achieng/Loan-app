package com.example.loanapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.User
import com.example.loanapplication.api.response.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel : ViewModel() {

    fun registerUser(user: User, onResult: (Boolean, String?) -> Unit) {
        val apiService =RetrofitClient.getApiService()
        val call = apiService.registerUser(user)

        call.enqueue(object: Callback<ApiResponse<String>>  {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful){
                    onResult(true,"Registration successful")
                }else{
                    onResult(false, "Registration failed. Status code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                onResult(false, "Registration failed. Error: ${t.message}")
            }

        })
    }
}