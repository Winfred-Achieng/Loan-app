package com.example.loanapplication

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.loanapplication.api.RetrofitClient
import com.example.loanapplication.api.User
import com.example.loanapplication.viewModel.AuthViewModel
import com.example.loanapplication.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            processFormFields()
        }

        binding.noAccountLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


     fun processFormFields(){
         if (!validateName() || !validatePhone()|| !validateEmail() || !validatePasswordAndConfirm()){
             return
         }
         binding.btnSignup.isEnabled = false
         binding.progressBar.visibility= View.VISIBLE

         val apiService = RetrofitClient.getApiService()

         val user = User(
             name = binding.etName.text.toString(),
             phone = binding.etPhone.text.toString(),
             email = binding.etEmail.text.toString(),
             password = binding.etPswd.text.toString()
         )

         val call = apiService.registerUser(user)

         call.enqueue(object :retrofit2.Callback<String> {
             override fun onResponse(call: Call<String>, response: Response<String>) {
                 binding.btnSignup.isEnabled=true
                 binding.progressBar.visibility =View.GONE

                 if (response.isSuccessful) {
                     Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_LONG).show()
                     val intent = Intent(applicationContext, LoginActivity::class.java)
                     startActivity(intent)
                     finish()
                 } else {
                     Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_LONG).show()
                 }
             }

             override fun onFailure(call: Call<String>, t: Throwable) {
                 binding.btnSignup.isEnabled=true
                 binding.progressBar.visibility =View.GONE

                 Toast.makeText(applicationContext, "Registration failed. Check your internet connection.", Toast.LENGTH_LONG).show()
                 t.printStackTrace()
             }
         })

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

}