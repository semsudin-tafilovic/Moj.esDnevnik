package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_student_select.view.*
import rs.tafilovic.mojesdnevnik.R
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
            tvName.text = student.fullName
            tvUniqueNumber.text = student.jmbg
            tvSchool.text =
                "${student.getSchool().schoolName} (${student.getSchoolYear().classes.values.first().section})"
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