package com.ute.studentmvvm.network

import com.google.gson.annotations.SerializedName
import com.ute.studentmvvm.model.Student

data class StudentDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("student_code")
    val studentCode: String,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("gpa")
    val gpa: Double? = null,

    @SerializedName("major")
    val major: String? = null
) {
    fun toEntity(): Student {
        val safeEmail = if (!email.isNullOrBlank()) email else "${studentCode.lowercase()}@ute.udn.vn"
        return Student(
            fullName = fullName,
            studentCode = studentCode,
            email = safeEmail,
            gpa = gpa ?: 0.0,
            major = major ?: "Công Nghệ Thông Tin"
        )
    }
}
