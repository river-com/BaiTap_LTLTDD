package com.example.recyclerview.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.R
import com.example.recyclerview.databinding.DialogAddStudentBinding
import com.example.recyclerview.databinding.FragmentStudentListBinding
import com.example.recyclerview.model.Student
import com.example.recyclerview.ui.adapter.StudentListAdapter
import com.example.recyclerview.viewmodel.StudentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.UUID

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var adapter: StudentListAdapter

    // Danh sách màu avatar đẹp để gán ngẫu nhiên cho sinh viên mới
    private val avatarColors = listOf(
        Color.parseColor("#3F51B5"),
        Color.parseColor("#E91E63"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#795548")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = StudentListAdapter(
            onItemClick = { student ->
                openStudentDetail(student)
            },
            onDeleteClick = { student ->
                confirmDeleteStudent(student)
            }
        )

        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.setHasFixedSize(true)
        binding.rvStudents.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.students.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isNullOrEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvStudents.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvStudents.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddStudent.setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun openStudentDetail(student: Student) {
        // Truyền STUDENT_ID thông qua Fragment Result API
        val resultBundle = bundleOf("STUDENT_ID" to student.id)
        parentFragmentManager.setFragmentResult("request_select_student", resultBundle)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, StudentDetailFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun confirmDeleteStudent(student: Student) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_msg)
            .setPositiveButton(R.string.delete_student) { _, _ ->
                viewModel.deleteStudent(student.id)
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    private fun showAddStudentDialog() {
        val dialogBinding = DialogAddStudentBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.btn_add, null) // Sẽ ghi đè listener để kiểm tra validation trước khi đóng
            .setNegativeButton(R.string.btn_cancel, null)
            .create()

        dialog.show()

        // Ghi đè onClick của nút Positive để không tự đóng dialog khi dữ liệu không hợp lệ
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (validateAndAddStudent(dialogBinding)) {
                dialog.dismiss()
            }
        }
    }

    private fun validateAndAddStudent(binding: DialogAddStudentBinding): Boolean {
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val mssv = binding.etMssv.text?.toString()?.trim().orEmpty()
        val gpaStr = binding.etGpa.text?.toString()?.trim().orEmpty()
        val major = binding.etMajor.text?.toString()?.trim().orEmpty()

        var isValid = true

        // Validate Họ tên
        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.error_name_empty)
            isValid = false
        } else {
            binding.tilName.error = null
        }

        // Validate MSSV
        if (mssv.isEmpty()) {
            binding.tilMssv.error = getString(R.string.error_mssv_empty)
            isValid = false
        } else if (viewModel.isMssvExists(mssv)) {
            binding.tilMssv.error = getString(R.string.error_mssv_exists)
            isValid = false
        } else {
            binding.tilMssv.error = null
        }

        // Validate GPA
        val gpaVal = gpaStr.toDoubleOrNull()
        if (gpaStr.isEmpty()) {
            binding.tilGpa.error = getString(R.string.error_gpa_empty)
            isValid = false
        } else if (gpaVal == null || gpaVal < 0.0 || gpaVal > 4.0) {
            binding.tilGpa.error = getString(R.string.error_gpa_invalid)
            isValid = false
        } else {
            binding.tilGpa.error = null
        }

        // Validate Chuyên ngành
        if (major.isEmpty()) {
            binding.tilMajor.error = getString(R.string.error_major_empty)
            isValid = false
        } else {
            binding.tilMajor.error = null
        }

        if (isValid && gpaVal != null) {
            val randomColor = avatarColors.random()
            val newStudent = Student(
                id = UUID.randomUUID().toString(),
                mssv = mssv,
                name = name,
                gpa = gpaVal,
                major = major,
                avatarColor = randomColor
            )
            viewModel.addStudent(newStudent)
            return true
        }

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
