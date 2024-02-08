package com.example.loanapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.loanapplication.api.ResendOtpRequest
import com.example.loanapplication.api.VerificationRequest
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpViewModel : ViewModel() {

    fun resendOtp(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val apiService = RetrofitClient.getApiService()
        val resendOtpRequest = ResendOtpRequest(email = email)
        val call = apiService.resendOtp(resendOtpRequest)

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse?.success == true) {
                        onSuccess.invoke()
                    } else {
                        onError.invoke("Failed to resend OTP: ${apiResponse?.message}")
                    }
                } else {
                    onError.invoke("HTTP error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                onError.invoke("Network failure: ${t.message}")
            }
        })
    }

    fun verifyOtp(
        enteredOtp: String,
        email: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val apiService = RetrofitClient.getApiService()
        val verificationRequest = VerificationRequest(enteredOtp = enteredOtp, email = email)
        val call = apiService.verifyOtp(verificationRequest)

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        onSuccess.invoke()
                    } else {
                        onError.invoke()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                onError.invoke()
            }
        })
    }
}
