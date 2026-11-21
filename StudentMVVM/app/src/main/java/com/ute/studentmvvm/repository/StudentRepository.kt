package com.ute.studentmvvm.repository

import com.ute.studentmvvm.data.StudentDao
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.network.RetrofitClient
import com.ute.studentmvvm.network.StudentApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
    private val apiService: StudentApiService = RetrofitClient.apiService
) {

    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student): Long {
        return studentDao.insertStudent(student)
    }

    suspend fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }

    suspend fun deleteById(id: Int) {
        studentDao.deleteStudentById(id)
    }

    suspend fun syncFromCloud(): Result<Int> {
        return try {
            val dtoList = apiService.getStudents()
            var syncCount = 0
            for (dto in dtoList) {
                val entity = dto.toEntity()
                val existing = studentDao.getStudentByCode(entity.studentCode)
                if (existing != null) {
                    val updatedEntity = entity.copy(id = existing.id)
                    studentDao.updateStudent(updatedEntity)
                } else {
                    studentDao.insertStudent(entity)
                }
                syncCount++
            }
            Result.success(syncCount)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}
