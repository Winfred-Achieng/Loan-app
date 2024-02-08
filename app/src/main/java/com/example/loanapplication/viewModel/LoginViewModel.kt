package com.example.loanapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.loanapplication.api.LoginRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.response.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel:ViewModel() {

    fun loginUser(loginRequest: LoginRequest, onResult: (Boolean, String?) -> Unit){
        val apiService = RetrofitClient.getApiService()
        val call = apiService.loginUser(loginRequest)

        call.enqueue(object: Callback<ApiResponse<String>>{
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse?.success == true) {
                        val username = apiResponse.data
                        onResult(true, username)
                    } else {
                        val errorMessage = apiResponse?.message ?: "Unknown error"
                        onResult(false, errorMessage)
                    }
                }   else{
                    onResult(false, "Invalid credentials")
                }
                    }



            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                onResult(false, "Network failure")
            }

        })
    }
}