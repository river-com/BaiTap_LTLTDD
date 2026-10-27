package com.example.events

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.events.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        val name = intent.getStringExtra("EXTRA_NAME") ?: ""
        val email = intent.getStringExtra("EXTRA_EMAIL") ?: ""
        val phone = intent.getStringExtra("EXTRA_PHONE") ?: ""
        val gender = intent.getStringExtra("EXTRA_GENDER") ?: ""
        val social = intent.getStringExtra("EXTRA_SOCIAL") ?: "Không chọn"

        // Hiển thị lên giao diện
        binding.tvDetailName.text = "Họ và tên: $name"
        binding.tvDetailEmail.text = "Email: $email"
        binding.tvDetailPhone.text = "Số điện thoại: $phone"
        binding.tvDetailGender.text = "Giới tính: $gender"
        binding.tvDetailSocial.text = "Mạng xã hội: $social"

        // Nút quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
