package com.example.fragmenver2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.fragmenver2.databinding.FragmentStudentDetailBinding

class StudentDetailFragment : Fragment() {

    private var _binding: FragmentStudentDetailBinding? = null
    private val binding get() = _binding!!

    private var studentId: Int? = null
    private var studentName: String? = null
    private var studentClass: String? = null
    private var studentGpa: Double? = null
    private var hasData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            hasData = savedInstanceState.getBoolean(KEY_HAS_DATA, false)
            if (hasData) {
                studentId = savedInstanceState.getInt(KEY_ID)
                studentName = savedInstanceState.getString(KEY_NAME)
                studentClass = savedInstanceState.getString(KEY_CLASS)
                studentGpa = savedInstanceState.getDouble(KEY_GPA)
            }
        }

        setFragmentResultListener("STUDENT_KEY") { _, bundle ->
            studentId = bundle.getInt("ID")
            studentName = bundle.getString("NAME")
            studentClass = bundle.getString("CLASS")
            studentGpa = bundle.getDouble("GPA")
            hasData = true
            updateUi()
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
        if (savedInstanceState != null) {
            hasData = savedInstanceState.getBoolean(KEY_HAS_DATA, false)
            if (hasData) {
                studentId = savedInstanceState.getInt(KEY_ID)
                studentName = savedInstanceState.getString(KEY_NAME)
                studentClass = savedInstanceState.getString(KEY_CLASS)
                studentGpa = savedInstanceState.getDouble(KEY_GPA)
            }
        }
        updateUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_HAS_DATA, hasData)
        studentId?.let { outState.putInt(KEY_ID, it) }
        studentName?.let { outState.putString(KEY_NAME, it) }
        studentClass?.let { outState.putString(KEY_CLASS, it) }
        studentGpa?.let { outState.putDouble(KEY_GPA, it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi() {
        if (_binding != null && hasData) {
            val details = """
                Mã số SV: ${studentId ?: ""}
                Họ và tên: ${studentName ?: ""}
                Lớp: ${studentClass ?: ""}
                Điểm GPA tích lũy: ${studentGpa ?: ""}
            """.trimIndent()
            binding.tvStudentDetail.text = details
        }
    }

    companion object {
        private const val KEY_HAS_DATA = "SAVED_HAS_DATA"
        private const val KEY_ID = "SAVED_ID"
        private const val KEY_NAME = "SAVED_NAME"
        private const val KEY_CLASS = "SAVED_CLASS"
        private const val KEY_GPA = "SAVED_GPA"
    }
}
