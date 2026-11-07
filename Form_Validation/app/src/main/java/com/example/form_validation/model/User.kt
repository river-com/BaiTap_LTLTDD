package com.example.form_validation.model

import java.io.Serializable

data class User(
    val fullName: String,
    val email: String,
    val phone: String
) : Serializable
