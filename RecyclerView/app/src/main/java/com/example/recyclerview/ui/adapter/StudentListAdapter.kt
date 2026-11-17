package com.example.recyclerview.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ItemStudentBinding
import com.example.recyclerview.model.Student

class StudentListAdapter(
    private val onItemClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentListAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
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
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.tvName.text = student.name
            binding.tvMssv.text = "MSSV: ${student.mssv}"
            binding.tvGpa.text = "GPA: ${student.gpa}"
            binding.tvMajor.text = student.major

            // Trích xuất chữ cái đầu tiên của tên làm avatar
            val nameParts = student.name.trim().split("\\s+".toRegex())
            val initial = nameParts.lastOrNull()?.firstOrNull()?.uppercase()
                ?: student.name.take(1).uppercase()
            binding.tvAvatar.text = initial

            // Đặt màu nền cho avatar
            val background = binding.tvAvatar.background
            if (background is GradientDrawable) {
                background.setColor(student.avatarColor)
            } else {
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(student.avatarColor)
                }
                binding.tvAvatar.background = drawable
            }

            // Gắn sự kiện click item và xóa
            binding.root.setOnClickListener {
                onItemClick(student)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(student)
            }
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }
    }
}
