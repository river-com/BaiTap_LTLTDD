package com.example.form_validation.validator

import android.util.Patterns

object FormValidator {

    fun validateFullName(fullName: String): String? {
        val trimmed = fullName.trim()
        if (trimmed.isEmpty()) {
            return "Họ và tên không được để trống"
        }
        if (trimmed.length < 3) {
            return "Họ và tên phải có ít nhất 3 ký tự"
        }
        return null
    }

    fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return "Email không được để trống"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            return "Email không đúng định dạng"
        }
        return null
    }

    fun validatePhone(phone: String): String? {
        val trimmed = phone.trim()
        if (trimmed.isEmpty()) {
            return "Số điện thoại không được để trống"
        }
        val phoneRegex = Regex("^0[35789]\\d{8}$")
        if (!phoneRegex.matches(trimmed)) {
            return "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 03, 05, 07, 08, 09"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Mật khẩu không được để trống"
        }
        if (password.length < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự"
        }
        return null
    }
}
