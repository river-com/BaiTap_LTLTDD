package com.ute.studentmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ute.studentmvvm.data.AppDatabase
import com.ute.studentmvvm.databinding.DialogAddStudentBinding
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.repository.StudentRepository
import com.ute.studentmvvm.viewmodel.StudentViewModel
import com.ute.studentmvvm.viewmodel.StudentViewModelFactory

class AddStudentDialogFragment : DialogFragment() {

    private var _binding: DialogAddStudentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels {
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val repository = StudentRepository(appDatabase.studentDao())
        StudentViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            saveStudent()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun saveStudent() {
        val fullName = binding.etFullName.text?.toString()?.trim().orEmpty()
        val studentCode = binding.etStudentCode.text?.toString()?.trim().orEmpty()
        val emailInput = binding.etEmail.text?.toString()?.trim().orEmpty()
        val gpaInput = binding.etGpa.text?.toString()?.trim().orEmpty()

        var isValid = true

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Vui lòng nhập họ và tên"
            isValid = false
        } else {
            binding.etFullName.error = null
        }

        if (studentCode.isEmpty()) {
            binding.etStudentCode.error = "Vui lòng nhập MSSV"
            isValid = false
        } else {
            binding.etStudentCode.error = null
        }

        if (!isValid) return

        val email = if (emailInput.isNotEmpty()) {
            emailInput
        } else {
            "${studentCode.lowercase()}@ute.udn.vn"
        }

        val gpa = gpaInput.toDoubleOrNull() ?: 0.0

        val student = Student(
            fullName = fullName,
            studentCode = studentCode,
            email = email,
            gpa = gpa,
            major = "Công Nghệ Thông Tin"
        )

        viewModel.insert(student)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
