package com.example.loanapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loanapplication.api.CountdownCallback
import com.example.loanapplication.databinding.ActivityOtpactivityBinding
import com.example.loanapplication.viewModel.OtpViewModel

class OTPActivity : AppCompatActivity(), CountdownCallback {

    private lateinit var binding: ActivityOtpactivityBinding
    private val otpViewModel: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }

        binding.tvResendOtp.setOnClickListener {
            resendOtp()
        }
    }

    private fun resendOtp() {
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = intent.getStringExtra("user_email") ?: ""

        otpViewModel.resendOtp(email,
            onSuccess = {
                showToast("OTP resent successfully")
                startCountdownTimer()
            },
            onError = { errorMessage ->
                showToast(errorMessage)
            })
    }

    private fun verifyOtp() {
        val email = intent.getStringExtra("user_email") ?: ""
        val enteredOtp = binding.etOtp.text.toString()

        otpViewModel.verifyOtp(enteredOtp, email,
            onSuccess = {
                showToast("OTP verified successfully")

                val userName = intent.getStringExtra("user_name")
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("user_name", userName)
                startActivity(intent)
                finish()
            },
            onError = {
                showToast("OTP verification failed")
            })
    }

    private fun startCountdownTimer() {
        val cooldownMillis: Long = 30 * 1000
        val countdownInterval: Long = 1000

        object : CountDownTimer(cooldownMillis, countdownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val PREFS_NAME = "MyPrefs"
    }
}
