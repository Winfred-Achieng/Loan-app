package com.example.loanapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loanapplication.api.PasswordResetRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.databinding.ActivityOtp2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Otp2Activity : AppCompatActivity() {
    private  lateinit var binding:ActivityOtp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtp2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnVerifyRestPasswordOtp.setOnClickListener {
            authenticateOtpField()
        }
    }

    fun authenticateOtpField() {
        if (!validateOtpField()) {
            return
        }
        val enteredOtp = binding.etOtp.text.toString()
        val email = intent.getStringExtra("email")


        val apiService= RetrofitClient.getApiService()
        val resetRequest = email?.let {
            PasswordResetRequest(
                email= it,
                passwordOtp = enteredOtp,
                newPassword="")
        }

        val call = resetRequest?.let { apiService.verifyPasswordResetOtp(it) }

        call!!.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"Otp verified", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, ChangePasswordActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otp", enteredOtp)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Error verifying ", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("OtpPasswordReset","Network Failure")

            }
        })
    }

    private fun validateOtpField (): Boolean {
        val phone = binding.etOtp.text.toString()
        if(phone.isEmpty()){
            binding.etOtp.error = "Otp cannot be empty"
            return false
        }
        else{
            binding.etOtp.error =null
            return true
        }     }


}