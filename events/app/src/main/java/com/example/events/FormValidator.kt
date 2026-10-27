package com.example.events

import android.util.Patterns

object FormValidator {

    fun isValidName(name: String): Boolean {
        val trimmed = name.trim()
        return trimmed.length >= 3
    }

    fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        return trimmed.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        val trimmed = phone.trim()
        // 10 digits, starting with 03, 05, 07, 08, 09
        val phoneRegex = Regex("^0[35789]\\d{8}$")
        return phoneRegex.matches(trimmed)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
