package com.example.loanapplication

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.loanapplication.api.LoginRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.databinding.ActivityLoginBinding
import com.example.loanapplication.viewModel.AuthViewModel
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    private companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val TOKEN_KEY = "token"
    }

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        @RequiresApi(Build.VERSION_CODES.P)
        get() = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication success!")
                val username=intent.getStringExtra("user_name")
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("user_name", username)
                startActivity(intent)
                finish()
            }
        }

//    @RequiresApi(Build.VERSION_CODES.P)
//    private fun requestUsernameAfterBiometricAuthentication() {
//        val apiService = RetrofitClient.getApiService()
//
//        val email = binding.etEmailOrPhone.text.toString()
//        val token = sharedPreferences.getString(TOKEN_KEY, "")
//
//            val call = apiService.getUsername(email)
//
//            call.enqueue(object : retrofit2.Callback<ApiResponse<String>> {
//                override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
//                    if (response.isSuccessful) {
//                        val apiResponse = response.body()
//
//                        if (apiResponse?.success == true) {
//                            val username = apiResponse.data
//
//                            if (username != null) {
//                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                intent.putExtra("user_name", username)
//                                startActivity(intent)
//                                finish()
//                            } else {
//                                notifyUser("Username is null")
//                            }
//                        } else {
//                            notifyUser("Failed to get username: ${apiResponse?.message}")
//                        }
//                    } else {
//                        notifyUser("Error occurred while getting username")
//                    }
//                }
//
//                override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
//                    notifyUser("Network failure while getting username")
//                }
//            })
//
//    }



    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkBiometricSupport()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Biometric
        val authentication = binding.fingerprint
        authentication.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Login")
                .setSubtitle("Login to your account ")
                .setDescription("This app uses fingerprint protection to keep your data secure")
                .setNegativeButton("Cancel", this.mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                        notifyUser("Authentication cancelled")
                    }).build()
            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
        }

        // Sign-in
        binding.btnSignin.setOnClickListener {
            authenticateUser()
        }

        // Toggling password visibility
        val passwordEditText = binding.etPswd
        val eyeOpenDrawable = ContextCompat.getDrawable(this, R.drawable.ic_eye_open)
        val eyeClosedDrawable = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)
        var isPasswordVisible = false

        eyeClosedDrawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            eyeOpenDrawable?.setBounds(0, 0, eyeOpenDrawable.intrinsicWidth, eyeOpenDrawable.intrinsicHeight)

            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, eyeClosedDrawable, null
            )

            passwordEditText.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val bounds = eyeClosedDrawable?.bounds
                    if (event.rawX >= passwordEditText.right - bounds?.width()!!) {
                        isPasswordVisible = !isPasswordVisible
                        val newDrawable = if (isPasswordVisible) eyeOpenDrawable else eyeClosedDrawable
                        passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            null, null, newDrawable, null
                        )
                        passwordEditText.transformationMethod =
                            if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
                        return@setOnTouchListener true
                    }
                }
                false
            }
        }

        // Forgot password
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Modifying the sign-up text
        val completeText = binding.noAccountSignup.text.toString()
        val underlinedText = "Sign Up"
        val startIndex = completeText.indexOf(underlinedText)

        if (startIndex != -1) {
            val endIndex = startIndex + underlinedText.length
            val blueColor = ContextCompat.getColor(this, R.color.blue)

            val spannableString = SpannableString(completeText)
            spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(blueColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.noAccountSignup.text = spannableString

            binding.noAccountSignup.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            binding.noAccountSignup.setTextColor(Color.BLACK)
        }
    }

    fun authenticateUser() {
        if (!validateEmailOrPhone() || !validatePassword()) {
            return
        }
        val apiService = RetrofitClient.getApiService()
        val loginRequest = LoginRequest(
            email = binding.etEmailOrPhone.text.toString(),
            phone = null,
            password = binding.etPswd.text.toString()
        )

        val call = apiService.loginUser(loginRequest)

        call.enqueue(object : retrofit2.Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse?.success == true) {
//                        val token = apiResponse.data
                        val username = apiResponse.data

                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("user_name", username)
//                        intent.putExtra("TOKEN_KEY", token)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("LoginActivity", "Login failed: ${apiResponse?.message}")

                        Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Toast.makeText(applicationContext, "Network failure", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateEmailOrPhone(): Boolean {
        val emailOrPhone = binding.etEmailOrPhone.text.toString()

        if (emailOrPhone.isEmpty()) {
            binding.etEmailOrPhone.error = "Provide Phone or Email"
            return false
        } else {
            binding.etEmailOrPhone.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.etPswd.text.toString()

        if (password.isEmpty()) {
            binding.etPswd.error = "Password cannot be empty"
            return false
        } else {
            binding.etPswd.error = null
            return true
        }
    }

    private fun saveToken(token: String?) {
        val editor = sharedPreferences.edit()
        token?.let {
            editor.putString("token", it)
        }
        editor.apply()
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in the settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}
