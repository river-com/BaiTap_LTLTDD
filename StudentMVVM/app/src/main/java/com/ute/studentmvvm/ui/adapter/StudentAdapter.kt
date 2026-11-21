package com.ute.studentmvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ute.studentmvvm.databinding.ItemStudentRoomBinding
import com.ute.studentmvvm.model.Student
import java.util.Locale

class StudentAdapter(
    private val onItemClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(StudentDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(
        private val binding: ItemStudentRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            val firstChar = student.fullName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "S"
            binding.tvAvatar.text = firstChar
            binding.tvFullName.text = student.fullName
            binding.tvStudentCode.text = "MSSV: ${student.studentCode}"
            binding.tvEmail.text = student.email
            binding.tvGpa.text = String.format(Locale.getDefault(), "GPA: %.2f", student.gpa)

            binding.root.setOnClickListener {
                onItemClick(student)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
        }
    }

    companion object {
        private val StudentDiffCallback = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }
        }
    }
}
