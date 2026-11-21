package com.ute.studentmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    val allStudents: LiveData<List<Student>> = repository.allStudents.asLiveData(Dispatchers.IO)

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _isSyncing = MutableLiveData<Boolean>()
    val isSyncing: LiveData<Boolean> get() = _isSyncing

    fun insert(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insert(student)
                _toastMessage.postValue("Thêm sinh viên thành công!")
            } catch (e: Exception) {
                _toastMessage.postValue("Lỗi thêm sinh viên: ${e.localizedMessage}")
            }
        }
    }

    fun delete(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.delete(student)
                _toastMessage.postValue("Đã xóa sinh viên ${student.fullName}")
            } catch (e: Exception) {
                _toastMessage.postValue("Lỗi xóa sinh viên: ${e.localizedMessage}")
            }
        }
    }

    fun syncFromCloud() {
        _isSyncing.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.syncFromCloud()
            result.onSuccess { count ->
                _toastMessage.postValue("Đồng bộ thành công $count sinh viên!")
            }.onFailure { error ->
                _toastMessage.postValue("Đồng bộ thất bại, đang dùng dữ liệu cục bộ: ${error.localizedMessage}")
            }
            _isSyncing.postValue(false)
        }
    }
}
