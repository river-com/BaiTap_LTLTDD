package com.example.baitap.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.baitap.databinding.FragmentStudentDetailBinding
import com.example.baitap.model.Student

class StudentDetailFragment : Fragment() {

    private var _binding: FragmentStudentDetailBinding? = null
    private val binding get() = _binding!!

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

        val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("student", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("student") as? Student
        }

        binding.tvDetailName.text = student?.name ?: "Nguyễn Ngọc Huy"
        binding.tvDetailCode.text = student?.msv ?: "24150513122119"
        binding.tvDetailBirthDate.text = student?.birthDate ?: "09/02/2006"

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
