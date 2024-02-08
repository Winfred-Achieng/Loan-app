package com.example.loanapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.loanapplication.databinding.ActivityRegisterBinding
import com.example.loanapplication.api.User
import com.example.loanapplication.api.response.ApiResponse
import com.example.loanapplication.api.RetrofitClient
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.loanapplication.viewModel.AuthViewModel
import com.example.loanapplication.viewModel.RegisterViewModel
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val icEyeClosedPswd = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)
        val icEyeClosedConfirmPswd = ContextCompat.getDrawable(this, R.drawable.ic_eye_closed)

        setupPasswordToggle(binding.etPswd,icEyeClosedPswd)
        setupPasswordToggle(binding.etConfirmPswd, icEyeClosedConfirmPswd)

        binding.btnSignup.setOnClickListener {
            processFormFields()
        }

        val completeText = binding.noAccountLogin.text.toString()
        val underlinedText = "Log in"
        val startIndex = completeText.indexOf(underlinedText)

        if (startIndex != -1) {
            val endIndex = startIndex + underlinedText.length
            val blueColor = ContextCompat.getColor(this, R.color.blue)

            val spannableString = SpannableString(completeText)
            spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(ForegroundColorSpan(blueColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.noAccountLogin.text = spannableString

            binding.noAccountLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            binding.noAccountLogin.setTextColor(Color.BLACK)
        }
    }

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

    private fun processFormFields() {
        if (!validateName() || !validatePhone() || !validateEmail() || !validatePasswordAndConfirm()) {
            return
        }

        binding.btnSignup.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        val user = User(
            name = binding.etName.text.toString(),
            phone = binding.etPhone.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPswd.text.toString()
        )

        registerViewModel.registerUser(user) { isSuccess, message ->
            binding.btnSignup.isEnabled = true
            binding.progressBar.visibility = View.GONE

            if (isSuccess) {
                val userEmail = binding.etEmail.text.toString()
                val userName = binding.etName.text.toString()
                val phone = binding.etPhone.text.toString()

                Toast.makeText(applicationContext, "Registration successful.", Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext, "Otp sent to  $userEmail and $phone", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, OTPActivity::class.java)
                intent.putExtra("user_email", userEmail)
                intent.putExtra("user_name", userName)
                startActivity(intent)
                finish()
            } else {
                Log.e("RegistrationActivity", message ?: "Unknown error")
                Toast.makeText(applicationContext, message ?: "Registration failed", Toast.LENGTH_LONG).show()
            }
        }
    }

//     fun processFormFields(){
//         if (!validateName() || !validatePhone()|| !validateEmail() || !validatePasswordAndConfirm()){
//             return
//         }
//         binding.btnSignup.isEnabled = false
//         binding.progressBar.visibility= View.VISIBLE
//
//         val apiService = RetrofitClient.getApiService()
//
//         val user = User(
//             name = binding.etName.text.toString(),
//             phone = binding.etPhone.text.toString(),
//             email = binding.etEmail.text.toString(),
//             password = binding.etPswd.text.toString()
//         )
//
//         val call = apiService.registerUser(user)
//
//         call.enqueue(object :retrofit2.Callback<ApiResponse<String>> {
//             override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
//                 binding.btnSignup.isEnabled=true
//                 binding.progressBar.visibility =View.GONE
//
//                 if (response.isSuccessful) {
//
//                     val responseBody = response.body()
//                     Log.d("Response Body", responseBody.toString())
//
//                     saveUserIdToPreferences(responseBody?.id ?: -1)
//
//                     val userEmail =binding.etEmail.text.toString()
//                     val userName =binding.etName.text.toString()
//
//                     Toast.makeText(applicationContext, "successful ", Toast.LENGTH_SHORT).show()
//                     Toast.makeText(applicationContext, "Otp sent to phone and email ", Toast.LENGTH_LONG).show()
//                     val intent = Intent(applicationContext, OTPActivity::class.java)
//                     intent.putExtra("user_email",userEmail)
//                     intent.putExtra("user_name",userName)
//                     startActivity(intent)
//                     finish()
//                 } else {
//                     Log.e("RegistrationActivity", "Unsuccessful response. Status code: ${response.code()}")
//                     Log.d("RegistrationActivity", "Registration failed. Status code: ${response.code()}")
//
//                     Toast.makeText(applicationContext, "Registration failed.", Toast.LENGTH_LONG).show()
//                 }
//             }
//
//             override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
//                 binding.btnSignup.isEnabled = true
//                 binding.progressBar.visibility = View.GONE
//
//                 Log.e("RegistrationFailure", "Error: ${t.message}")
//                 Log.e("RegistrationFailure", "Call details: $call")
//
//                 t.printStackTrace()
//
//                 Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_LONG).show()
//             }
//         })
//     }

    private fun saveUserIdToPreferences(userId: Long) {
        val prefs = applicationContext.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        prefs.edit().putLong(USER_ID,userId).apply()

    }

    //validations
     private fun validateName (): Boolean {
         val name = binding.etName.text.toString()
         if(name.isEmpty()){
             binding.etName.error = "First name cannot be empty"
             return false
         }else{
             binding.etName.error =null
             return true
         }     }

    private fun validatePhone (): Boolean {
        val phone = binding.etPhone.text.toString()
        if(phone.isEmpty()){
            binding.etPhone.error = "Phone cannot be empty"
            return false
        }else if (phone.length < 10){
            binding.etPhone.error = "Phone is not valid"
            return false        }
        else{
            binding.etPhone.error =null
            return true
        }     }

    private fun validateEmail (): Boolean {
        val email = binding.etEmail.text.toString()
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

    private fun validatePasswordAndConfirm (): Boolean {
        val password = binding.etPswd.text.toString()
        val confirm = binding.etConfirmPswd.text.toString()

        if(password.isEmpty() || confirm.isEmpty()){
            binding.etPswd.error = "Password cannot be empty"
            binding.etConfirmPswd.error = "Confirm Password cannot be empty"
            return false
        }else if (password.length < 6){
            binding.etPhone.error = "Password should be at least 6 characters"
            return false        }
        else if (password!=confirm){
            binding.etConfirmPswd.error = "Password do not match"
            return false        }
        else{
            binding.etPswd.error = null
            binding.etConfirmPswd.error =null
            return true
        }     }

    companion object {
        private const val USER_ID = "userId"
        private const val PREFS_NAME = "MyPrefs"
    }

}