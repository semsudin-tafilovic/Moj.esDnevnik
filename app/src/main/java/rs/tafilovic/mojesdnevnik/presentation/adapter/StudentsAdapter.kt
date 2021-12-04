package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.model.Student
import rs.tafilovic.mojesdnevnik.presentation.viewholder.StudentsViewHolder

class StudentsAdapter(val onStudentSelected: (Student) -> Unit) :
    ListAdapter<Student, StudentsViewHolder>(Student.DIF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        return StudentsViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onStudentSelected(getItem(position))
        }
    }
}