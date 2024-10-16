@file:Suppress("DEPRECATION")

package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.RowCourseTeachersBinding
import rs.tafilovic.mojesdnevnik.model.MainCourse
import rs.tafilovic.mojesdnevnik.model.Teacher

class CoursesAdapter : ListAdapter<MainCourse, CoursesViewHolder>(MainCourse.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        return CoursesViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CoursesViewHolder(private val itemBinding: RowCourseTeachersBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(course: MainCourse) {
        itemView.apply {
            itemBinding.tvName.text = course.name
            addTeachers(itemBinding.layoutContainer, course.teachers)
        }
    }

    private fun addTeachers(layout: LinearLayout, teachers: List<Teacher>) {

        layout.removeAllViews()

        for (t in teachers) {
            if (t.name.isNotEmpty()) {
                val tvTeacherName = TextView(layout.context).apply {
                    text = t.name
/*                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                        setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1)
                    else
                        setTextAppearance(R.style.TextAppearance_AppCompat_Body1)*/
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10, 10, 0, 0)
                tvTeacherName.layoutParams = layoutParams

                layout.addView(tvTeacherName)
            }

            if (!t.consultationInfo.isNullOrEmpty()) {
                val consultInfo="Konsultacije: ${t.consultationInfo}"
                val tvConsultationInfo = TextView(layout.context).apply {
                    text = consultInfo
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                        setTextAppearance(context, R.style.TextAppearance_AppCompat_Caption)
                    else
                        setTextAppearance(R.style.TextAppearance_AppCompat_Caption)
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10, 0, 0, 0)
                tvConsultationInfo.layoutParams = layoutParams
                layout.addView(tvConsultationInfo)
            }
        }
    }

    companion object {
        fun init(parent: ViewGroup): CoursesViewHolder {
            val itemBinding =
                RowCourseTeachersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CoursesViewHolder(itemBinding)
        }
    }
}