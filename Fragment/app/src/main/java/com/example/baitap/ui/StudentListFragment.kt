package com.example.baitap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baitap.R
import com.example.baitap.databinding.FragmentStudentListBinding
import com.example.baitap.model.Student

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

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

        val students = listOf(
            Student(
                name = "Nguyễn Ngọc Huy",
                msv = "24150513122119",
                birthDate = "09/02/2006"
            ),
            Student(
                name = "Trần Thị Bích",
                msv = "24150513122120",
                birthDate = "15/05/2006"
            ),
            Student(
                name = "Lê Văn Cường",
                msv = "24150513122121",
                birthDate = "20/11/2005"
            )
        )

        binding.rvStudentList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudentList.adapter = StudentAdapter(students) { student ->
            val bundle = bundleOf("student" to student)
            findNavController().navigate(
                R.id.action_StudentListFragment_to_StudentDetailFragment,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
