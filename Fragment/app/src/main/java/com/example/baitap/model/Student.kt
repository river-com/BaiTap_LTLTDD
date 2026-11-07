package com.example.baitap.model

import java.io.Serializable

data class Student(
    val name: String,
    val msv: String,
    val birthDate: String
) : Serializable
