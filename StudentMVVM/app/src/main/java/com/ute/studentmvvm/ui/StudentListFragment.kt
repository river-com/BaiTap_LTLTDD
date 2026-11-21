package com.ute.studentmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ute.studentmvvm.R
import com.ute.studentmvvm.databinding.FragmentStudentListBinding
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.ui.adapter.StudentAdapter
import com.ute.studentmvvm.viewmodel.StudentViewModel
import com.ute.studentmvvm.viewmodel.StudentViewModelFactory
import java.util.Locale

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels {
        val appDatabase = com.ute.studentmvvm.data.AppDatabase.getDatabase(requireContext())
        val repository = com.ute.studentmvvm.repository.StudentRepository(appDatabase.studentDao())
        StudentViewModelFactory(repository)
    }

    private lateinit var studentAdapter: StudentAdapter

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

        setupToolbar()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_student_list)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_sync) {
                viewModel.syncFromCloud()
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            onItemClick = { student -> showStudentDetailDialog(student) },
            onDeleteClick = { student -> showDeleteConfirmDialog(student) }
        )

        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studentAdapter
        }
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            AddStudentDialogFragment().show(parentFragmentManager, "AddStudentDialogFragment")
        }
    }

    private fun observeViewModel() {
        viewModel.allStudents.observe(viewLifecycleOwner) { students ->
            studentAdapter.submitList(students)
            binding.tvStudentCountBadge.text = "Tổng số sinh viên: ${students.size}"

            if (students.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvStudents.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvStudents.visibility = View.VISIBLE
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showStudentDetailDialog(student: Student) {
        val message = """
            Mã ID (Room): ${student.id}
            Họ tên: ${student.fullName}
            MSSV: ${student.studentCode}
            Email: ${student.email}
            GPA: ${String.format(Locale.getDefault(), "%.2f", student.gpa)}
            Ngành học: ${student.major}
        """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Chi tiết Sinh viên")
            .setMessage(message)
            .setPositiveButton("Đóng", null)
            .show()
    }

    private fun showDeleteConfirmDialog(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${student.fullName} (${student.studentCode})?")
            .setPositiveButton("Xóa") { _, _ ->
                viewModel.delete(student)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
