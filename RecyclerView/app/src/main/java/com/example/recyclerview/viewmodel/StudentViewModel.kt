package com.example.recyclerview.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.model.Student
import java.util.UUID

class StudentViewModel : ViewModel() {

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    init {
        // Dữ liệu sinh viên mẫu ban đầu
        val initialList = listOf(
            Student(
                id = UUID.randomUUID().toString(),
                mssv = "21110001",
                name = "Nguyễn Văn A",
                gpa = 3.5,
                major = "Công nghệ thông tin",
                avatarColor = Color.parseColor("#3F51B5")
            ),
            Student(
                id = UUID.randomUUID().toString(),
                mssv = "21110002",
                name = "Trần Thị B",
                gpa = 3.8,
                major = "Khoa học máy tính",
                avatarColor = Color.parseColor("#E91E63")
            ),
            Student(
                id = UUID.randomUUID().toString(),
                mssv = "21110003",
                name = "Lê Hoàng C",
                gpa = 3.2,
                major = "Hệ thống thông tin",
                avatarColor = Color.parseColor("#4CAF50")
            )
        )
        _students.value = initialList
    }

    /**
     * Thêm sinh viên mới.
     * Tạo một danh sách mới để trigger DiffUtil và không sửa trực tiếp danh sách cũ.
     */
    fun addStudent(student: Student) {
        val currentList = _students.value.orEmpty().toMutableList()
        currentList.add(student)
        _students.value = currentList
    }

    /**
     * Xóa sinh viên theo ID.
     * Tạo danh sách mới sau khi xóa mục tương ứng.
     */
    fun deleteStudent(studentId: String) {
        val currentList = _students.value.orEmpty().toMutableList()
        currentList.removeAll { it.id == studentId }
        _students.value = currentList
    }

    /**
     * Tìm sinh viên theo ID phục vụ màn hình chi tiết.
     */
    fun getStudentById(id: String): Student? {
        return _students.value?.find { it.id == id }
    }

    /**
     * Kiểm tra MSSV đã tồn tại hay chưa.
     */
    fun isMssvExists(mssv: String): Boolean {
        return _students.value?.any { it.mssv.equals(mssv.trim(), ignoreCase = true) } == true
    }
}
