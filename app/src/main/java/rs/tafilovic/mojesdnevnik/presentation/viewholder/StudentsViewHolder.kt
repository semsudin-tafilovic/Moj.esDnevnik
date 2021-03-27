package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.RowStudentSelectBinding
import rs.tafilovic.mojesdnevnik.model.School
import rs.tafilovic.mojesdnevnik.model.Student

class StudentsViewHolder(private val itemBinding: RowStudentSelectBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    val maleColor = Color.rgb(77, 138, 208)
    val femaleColor = Color.rgb(239, 100, 192)

    fun bind(student: Student) {

        val male = student.gender.equals("m", true)

        itemView.apply {

            itemBinding.ivIcon.apply {
                if (male) {
                    setImageResource(R.drawable.ic_student_male)
                    setColorFilter(maleColor)
                } else {
                    setImageResource(R.drawable.ic_student_female)
                    setColorFilter(femaleColor)
                }
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

            itemBinding.tvName.apply {
                text = student.fullName
                setTextColor(if (male) maleColor else femaleColor)
            }

            itemBinding.tvUniqueNumber.apply {
                text = student.jmbg
                setTextColor(if (male) maleColor else femaleColor)
            }

            itemBinding.tvSchool.text = String.format("%s %s", school?.schoolName, section)
            itemBinding.tvSchool.setTextColor(if (male) maleColor else femaleColor)
            itemBinding.tvSchool.alpha = 0.7f
        }
    }

    companion object {
        fun init(parent: ViewGroup): StudentsViewHolder {
            val itemBinding =
                RowStudentSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return StudentsViewHolder(itemBinding)
        }
    }
}