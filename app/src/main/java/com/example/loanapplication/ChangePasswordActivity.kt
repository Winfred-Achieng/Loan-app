package com.example.loanapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.loanapplication.api.PasswordResetRequest
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.databinding.ActivityChangePasswordBinding
import com.example.loanapplication.databinding.ActivityOtp2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityChangePasswordBinding

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitNewPswd.setOnClickListener {
            authenticatePassword()
        }

        val icEyeClosedPswd = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)
        val icEyeClosedConfirmPswd = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)

        setupPasswordToggle(binding.etNewPswd,icEyeClosedPswd)
        setupPasswordToggle(binding.etConfirmNewPswd, icEyeClosedConfirmPswd)

        binding.etNewPswd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                // Checking the length of the password
                val password = editable.toString()
                if (password.length < 6) {
                    binding.tvPasswordRequirement.visibility = View.VISIBLE
                } else {
                    binding.tvPasswordRequirement.visibility = View.INVISIBLE
                }
            }
        })

    }

    fun authenticatePassword() {
        if (!validatePasswordAndConfirm()) {
            return
        }
        val newPassword = binding.etNewPswd.text.toString()
        val confirmNewPassword = binding.etConfirmNewPswd.text.toString()
        val email = intent.getStringExtra("email")
        val enteredOtp = intent.getStringExtra("otp")

        val apiService=RetrofitClient.getApiService()
        val  resetRequest = email?.let {
            PasswordResetRequest(
                email = it,
                passwordOtp =enteredOtp!!,
                newPassword=newPassword
            )
        }
        val call = resetRequest?.let { apiService.verifyPasswordResetOtp(it) }

        call!!.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"Password reset successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otp", enteredOtp)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Error resetting password ", Toast.LENGTH_LONG).show()


                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("OtpPasswordReset", "Network Failure: ${t.message}")
                Log.e("OtpPasswordReset", "Network Failure", t)

            }

        })
    }

    private fun validatePasswordAndConfirm (): Boolean {
        val password = binding.etNewPswd .text.toString()
        val confirm = binding.etConfirmNewPswd.text.toString()

        if(password.isEmpty() || confirm.isEmpty()){
            binding.etNewPswd.error = "Password cannot be empty"
            binding.etConfirmNewPswd.error = "Confirm Password cannot be empty"
            return false
        }else if (password.length < 6){
            binding.etNewPswd.error = "Password should be at least 6 characters"
            return false        }
        else if (password!=confirm){
            binding.etConfirmNewPswd.error = "Password do not match"
            return false        }
        else{
            binding.etNewPswd.error = null
            binding.etConfirmNewPswd.error =null
            return true
        }     }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle(editText: EditText, eyeIcon: Drawable?) {
        val eyeOpenDrawable = ContextCompat.getDrawable(this, R.drawable.ic_eye_open)
        val eyeClosedDrawable = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)

        eyeClosedDrawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            eyeOpenDrawable?.setBounds(0, 0, eyeOpenDrawable.intrinsicWidth, eyeOpenDrawable.intrinsicHeight)

            editText.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val bounds = eyeClosedDrawable?.bounds
                    if (event.rawX >= editText.right - bounds?.width()!!) {
                        if (editText.transformationMethod == null) {
                            isPasswordVisible = !isPasswordVisible
                        } else {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }
                        val newDrawable = if (isPasswordVisible || isConfirmPasswordVisible) eyeOpenDrawable else eyeClosedDrawable
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            null, null, newDrawable, null
                        )
                        editText.transformationMethod =
                            if (isPasswordVisible || isConfirmPasswordVisible) null else PasswordTransformationMethod.getInstance()
                        return@setOnTouchListener true
                    }
                }
                false
            }
        }
    }

    }
