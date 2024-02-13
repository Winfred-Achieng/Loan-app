package com.example.loanapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.loanapplication.api.PasswordResetRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.viewModel.AuthViewModel
import com.example.loanapplication.databinding.ActivityForgotPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityForgotPasswordBinding
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnForgotpswd.setOnClickListener {
            authenticateEmail()
        }

        binding.backToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun authenticateEmail() {
        if (!validateEmail()) {
            return
        }

        val email = binding.etEmail.text.toString()
        val apiService = RetrofitClient.getApiService()
        val resetRequest = PasswordResetRequest(
            email = email,
            passwordOtp ="",
            newPassword = ""
        )
        val call = apiService.initiatePasswordReset(resetRequest)

        call.enqueue(object:Callback<ApiResponse<String>>{
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,"Otp sent to email for password reset",Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, Otp2Activity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                }

            }
            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("OtpPasswordReset","Network Failure")
            }

        })
    }
    private fun validateEmail (): Boolean {
        val email = binding.etEmail .text.toString()
        val pattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")

        if(email.isEmpty()){
            binding.etEmail.error = "Email cannot be empty"
            return false
        }else if (!pattern.matches(email)){
            binding.etEmail.error = "Email is not valid"
            return false        }
        else{
            binding.etEmail.error =null
            return true
        }     }


}