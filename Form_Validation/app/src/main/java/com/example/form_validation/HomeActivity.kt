package com.example.form_validation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.form_validation.databinding.ActivityHomeBinding
import com.example.form_validation.model.User

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra("USER_OBJECT") as? User
        if (user != null) {
            binding.tvGreeting.text = "Xin chào, ${user.fullName}!"
            binding.tvEmail.text = "Email: ${user.email}"
            binding.tvPhone.text = "Số điện thoại: ${user.phone}"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
