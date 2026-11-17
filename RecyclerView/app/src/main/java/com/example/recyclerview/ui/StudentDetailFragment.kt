package com.example.recyclerview.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.recyclerview.databinding.FragmentStudentDetailBinding
import com.example.recyclerview.model.Student
import com.example.recyclerview.viewmodel.StudentViewModel

class StudentDetailFragment : Fragment() {

    private var _binding: FragmentStudentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels()

    private var currentStudentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lắng nghe Fragment Result API từ StudentListFragment
        parentFragmentManager.setFragmentResultListener("request_select_student", this) { _, bundle ->
            val studentId = bundle.getString("STUDENT_ID")
            if (studentId != null) {
                currentStudentId = studentId
                loadStudentDetail(studentId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarDetail.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Nếu đã có ID (ví dụ khi Fragment được tạo lại hoặc nạp từ Listener), hiển thị thông tin
        currentStudentId?.let { id ->
            loadStudentDetail(id)
        }
    }

    private fun loadStudentDetail(studentId: String) {
        if (_binding == null) return
        val student = viewModel.getStudentById(studentId) ?: return
        displayStudent(student)
    }

    private fun displayStudent(student: Student) {
        binding.tvNameDetail.text = student.name
        binding.tvMssvDetail.text = student.mssv
        binding.tvGpaDetail.text = student.gpa.toString()
        binding.tvMajorDetail.text = student.major

        // Chữ cái đầu tiên trên avatar
        val nameParts = student.name.trim().split("\\s+".toRegex())
        val initial = nameParts.lastOrNull()?.firstOrNull()?.uppercase()
            ?: student.name.take(1).uppercase()
        binding.tvAvatarDetail.text = initial

        // Đặt màu nền avatar
        val background = binding.tvAvatarDetail.background
        if (background is GradientDrawable) {
            background.setColor(student.avatarColor)
        } else {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(student.avatarColor)
            }
            binding.tvAvatarDetail.background = drawable
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
