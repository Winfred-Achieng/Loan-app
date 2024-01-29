package com.example.loanapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.loanapplication.api.CountdownCallback
import com.example.loanapplication.api.ResendOtpRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.VerificationRequest
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.databinding.ActivityOtpactivityBinding
import com.example.loanapplication.viewModel.AuthViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity(), CountdownCallback {

    private lateinit var binding: ActivityOtpactivityBinding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }
        binding.tvResendOtp.setOnClickListener {
            resendOtp(this)
        }
    }

    private fun resendOtp(callback: CountdownCallback) {
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getLong(USER_ID, -1)
        val email =intent.getStringExtra("user_email") ?: ""

        val apiService= RetrofitClient.getApiService()
        val resendOtpRequest = ResendOtpRequest(
            email = email)
        val call = apiService.resendOtp(resendOtpRequest)

        val cooldownMillis: Long = 30 * 1000
        val countdownInterval: Long = 1000

        object: CountDownTimer(cooldownMillis, countdownInterval){
            override fun onTick(millisUntilFinished: Long) {
                callback.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                callback.onFinish()
                call.enqueue(object : retrofit2.Callback<ApiResponse<String>> {
                    override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()

                            if (apiResponse?.success == true) {
                                showToast("OTP resent successfully")
                            } else {
                                    showToast("Failed to resend OTP: ${apiResponse?.message}")
                            }
                        } else {
                            showToast("HTTP error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                        showToast("Network failure: ${t.message}")
                    }
                })
            }
        }   .start()
    }


    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun verifyOtp() {
        val email =intent.getStringExtra("user_email") ?: ""
        val apiService = RetrofitClient.getApiService()
        val verificationRequest = VerificationRequest(
            enteredOtp = binding.etOtp.text.toString(),
            email = email
        )
        val call = apiService.verifyOtp(verificationRequest)

        call.enqueue(object : retrofit2.Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {

                        Toast.makeText(applicationContext, "OTP verified successfully", Toast.LENGTH_SHORT).show()

                        val userName = intent.getStringExtra("user_name")
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.putExtra("user_name", userName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "OTP verification failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Toast.makeText(applicationContext, "Network failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val USER_ID = "userId"
        private const val PREFS_NAME = "MyPrefs"
    }

    override fun onTick(millisUntilFinished: Long) {
        val secondsRemaining = millisUntilFinished / 1000
        binding.tvRemainingTime.text = "Resend in: $secondsRemaining seconds"
        binding.tvResendOtp.visibility = View.GONE
    }

    override fun onFinish() {
        binding.tvRemainingTime.text = ""
        binding.tvResendOtp.visibility = View.VISIBLE
    }


}