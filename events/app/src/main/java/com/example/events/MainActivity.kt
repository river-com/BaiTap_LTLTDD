package com.example.events

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.events.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentToast: Toast? = null

    private fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupListeners()
    }

    private fun setupTextWatchers() {
        // Họ và tên
        binding.etName.doAfterTextChanged { _ ->
            if (binding.tilName.error != null) {
                binding.tilName.error = null
                binding.tilName.isErrorEnabled = false
            }
            showToast("Đang nhập Họ và tên...")
        }

        // Email
        binding.etEmail.doAfterTextChanged { _ ->
            if (binding.tilEmail.error != null) {
                binding.tilEmail.error = null
                binding.tilEmail.isErrorEnabled = false
            }
            showToast("Đang nhập Email...")
        }

        // Số điện thoại
        binding.etPhone.doAfterTextChanged { _ ->
            if (binding.tilPhone.error != null) {
                binding.tilPhone.error = null
                binding.tilPhone.isErrorEnabled = false
            }
            showToast("Đang nhập Số điện thoại...")
        }

        // Mật khẩu (KHÔNG hiển thị mật khẩu trong Toast)
        binding.etPassword.doAfterTextChanged { _ ->
            if (binding.tilPassword.error != null) {
                binding.tilPassword.error = null
                binding.tilPassword.isErrorEnabled = false
            }
            showToast("Đang nhập Mật khẩu...")
        }
    }

    private fun setupListeners() {
        // RadioGroup giới tính
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            rb?.let {
                showToast("Đã chọn giới tính: ${it.text}")
            }
        }

        // CheckBox mạng xã hội
        binding.cbFacebook.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Đã chọn Facebook" else "Đã bỏ chọn Facebook"
            showToast(status)
        }

        binding.cbInstagram.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Đã chọn Instagram" else "Đã bỏ chọn Instagram"
            showToast(status)
        }

        binding.cbTiktok.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Đã chọn TikTok" else "Đã bỏ chọn TikTok"
            showToast(status)
        }

        // Nút Đăng ký
        binding.btnRegister.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()

        var isValid = true

        // Kiểm tra Họ tên
        if (!FormValidator.isValidName(name)) {
            binding.tilName.error = "Họ và tên phải có ít nhất 3 ký tự"
            isValid = false
        } else {
            binding.tilName.error = null
            binding.tilName.isErrorEnabled = false
        }

        // Kiểm tra Email
        if (!FormValidator.isValidEmail(email)) {
            binding.tilEmail.error = "Email không đúng định dạng"
            isValid = false
        } else {
            binding.tilEmail.error = null
            binding.tilEmail.isErrorEnabled = false
        }

        // Kiểm tra Số điện thoại
        if (!FormValidator.isValidPhone(phone)) {
            binding.tilPhone.error = "SĐT gồm 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09"
            isValid = false
        } else {
            binding.tilPhone.error = null
            binding.tilPhone.isErrorEnabled = false
        }

        // Kiểm tra Mật khẩu
        if (!FormValidator.isValidPassword(password)) {
            binding.tilPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        } else {
            binding.tilPassword.error = null
            binding.tilPassword.isErrorEnabled = false
        }

        // Kiểm tra Giới tính
        val selectedGenderId = binding.rgGender.checkedRadioButtonId
        if (selectedGenderId == -1) {
            showToast("Vui lòng chọn giới tính!")
            isValid = false
        }

        if (!isValid) return

        // Lấy giới tính được chọn
        val selectedGenderRb = findViewById<RadioButton>(selectedGenderId)
        val genderStr = selectedGenderRb?.text.toString()

        // Lấy danh sách mạng xã hội đã chọn
        val socialList = mutableListOf<String>()
        if (binding.cbFacebook.isChecked) socialList.add("Facebook")
        if (binding.cbInstagram.isChecked) socialList.add("Instagram")
        if (binding.cbTiktok.isChecked) socialList.add("TikTok")

        val socialStr = if (socialList.isNotEmpty()) {
            socialList.joinToString(", ")
        } else {
            "Không chọn"
        }

        // Chuyển sang màn hình chi tiết bằng Intent
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("EXTRA_NAME", name.trim())
            putExtra("EXTRA_EMAIL", email.trim())
            putExtra("EXTRA_PHONE", phone.trim())
            putExtra("EXTRA_GENDER", genderStr)
            putExtra("EXTRA_SOCIAL", socialStr)
        }
        startActivity(intent)
    }
}
