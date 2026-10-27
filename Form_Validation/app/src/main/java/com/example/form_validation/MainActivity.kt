package com.example.form_validation

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.form_validation.databinding.ActivityMainBinding
import com.example.form_validation.model.User
import com.example.form_validation.validator.FormValidator
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupListeners()
    }

    private fun setupTextWatchers() {
        binding.etFullName.doAfterTextChanged {
            binding.tilFullName.error = null
        }
        binding.etEmail.doAfterTextChanged {
            binding.tilEmail.error = null
        }
        binding.etPhone.doAfterTextChanged {
            binding.tilPhone.error = null
        }
        binding.etPassword.doAfterTextChanged {
            binding.tilPassword.error = null
        }
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            // Chống bấm liên tiếp (1 giây)
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return@setOnClickListener
            }
            lastClickTime = SystemClock.elapsedRealtime()

            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()

            val fullNameError = FormValidator.validateFullName(fullName)
            val emailError = FormValidator.validateEmail(email)
            val phoneError = FormValidator.validatePhone(phone)
            val passwordError = FormValidator.validatePassword(password)

            var hasError = false

            if (fullNameError != null) {
                binding.tilFullName.error = fullNameError
                hasError = true
            }
            if (emailError != null) {
                binding.tilEmail.error = emailError
                hasError = true
            }
            if (phoneError != null) {
                binding.tilPhone.error = phoneError
                hasError = true
            }
            if (passwordError != null) {
                binding.tilPassword.error = passwordError
                hasError = true
            }

            if (hasError) {
                Snackbar.make(binding.root, "Vui lòng kiểm tra lại thông tin", Snackbar.LENGTH_LONG).show()
            } else {
                val user = User(fullName.trim(), email.trim(), phone.trim())
                val intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("USER_OBJECT", user)
                }
                startActivity(intent)
            }
        }

        binding.btnCancel.setOnClickListener {
            clearForm()
        }
    }

    private fun clearForm() {
        binding.etFullName.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPhone.text?.clear()
        binding.etPassword.text?.clear()

        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilPassword.error = null

        binding.etFullName.requestFocus()
    }
}
