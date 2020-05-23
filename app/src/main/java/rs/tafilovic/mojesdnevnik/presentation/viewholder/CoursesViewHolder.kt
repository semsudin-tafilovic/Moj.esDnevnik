package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_course_teachers.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.MainCourse
import rs.tafilovic.mojesdnevnik.model.Teacher

@Suppress("DEPRECATION")
class CoursesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(course: MainCourse) {
        itemView.apply {
            tvName.text = course.name
            addTeachers(layoutContainer, course.teachers)
        }
    }

    fun addTeachers(layout: LinearLayout, teachers: List<Teacher>) {

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
                val tvConsultationInfo = TextView(layout.context).apply {
                    text = "Konsultacije: ${t.consultationInfo}"
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
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_course_teachers, parent, false)
            return CoursesViewHolder(view)
        }
    }
}