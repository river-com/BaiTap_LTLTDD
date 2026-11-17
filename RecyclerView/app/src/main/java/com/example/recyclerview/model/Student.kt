package com.example.recyclerview.model

/**
 * Data model cho Sinh viên theo đúng yêu cầu slide:
 * - id: String (ID duy nhất phục vụ DiffUtil)
 * - mssv: String
 * - name: String
 * - gpa: Double
 * - major: String
 * - avatarColor: Int (Mã màu hiển thị avatar)
 */
data class Student(
    val id: String,
    val mssv: String,
    val name: String,
    val gpa: Double,
    val major: String,
    val avatarColor: Int
)
