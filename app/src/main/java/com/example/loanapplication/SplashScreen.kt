package com.example.loanapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.loanapplication.viewModel.AuthViewModel
import com.example.loanapplication.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val preferences = getPreferences(Context.MODE_PRIVATE)
            val onboardingShown = preferences.getBoolean("onboarding_shown", false)
            Log.d("Preferences", "onboarding_shown: $onboardingShown")

            if (!onboardingShown) {
                navigateToWalkthroughActivity()
            } else {
                navigateToLoginActivity()
            }
        }, 3000)
    }

    private fun navigateToWalkthroughActivity() {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        preferences.edit().putBoolean("onboarding_shown", true).apply()

        val intent = Intent(this, WalkthroughActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
