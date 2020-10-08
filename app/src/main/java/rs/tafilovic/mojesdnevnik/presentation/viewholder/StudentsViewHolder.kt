package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_student_select.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.Student

class StudentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(student: Student) {
        itemView.apply {
            ivIcon.apply {
                setImageResource(
                    if (student.gender.equals(
                            "m",
                            true
                        )
                    ) R.drawable.ic_student_male else R.drawable.ic_student_female
                )
                setColorFilter(Color.DKGRAY)
            }

            val schools = student.schools.entries.sortedByDescending {
                it.key.toInt()
            }.map { it.value }

            val school: School? = if (schools.isNotEmpty()) schools[0] else null

            val schoolClass = school?.schoolyears?.entries?.sortedByDescending { it.key.toInt() }
                ?.map { it.value.classes.entries.sortedByDescending { it.key.toInt() } }

            val section = schoolClass?.get(0)
                ?.sortedByDescending { it.key.toInt() }
                ?.map { it.value.section }

            tvName.text = student.fullName
            tvUniqueNumber.text = student.jmbg
            tvSchool.text = "${school?.schoolName} (${section})"
        }
    }

    companion object {
        fun init(parent: ViewGroup): StudentsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_student_select, parent, false)
            return StudentsViewHolder(view)
        }
    }
}