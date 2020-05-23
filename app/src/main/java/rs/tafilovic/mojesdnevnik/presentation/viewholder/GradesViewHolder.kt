package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_subject_activity.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.FullGrade
import rs.tafilovic.mojesdnevnik.model.FullGrades

class GradesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val napredovanje = "Напредовање\n"
    val angazovanje = "\nАнгажовање\n"

    fun bind(fg: FullGrades) {
        itemView.tvClassName.text = fg.course

        val gradeTypeId = fg.classCourseGradeTypeId

        val partOneFinalGrade = fg.parts["1"]?.final?.value ?: "/"
        val partOneAverageGrade = fg.parts["1"]?.average ?: 0f
        val partOneFinalName = fg.parts["1"]?.final?.name
        val partOneFinalEngagement = fg.parts["1"]?.final?.engagement

        val partTwoFinalGrade = fg.parts["2"]?.final?.value ?: "/"
        val partTwoAverageGrade = fg.parts["2"]?.average ?: 0f
        val partTwoFinalName = fg.parts["2"]?.final?.name
        val partTwoFinalEngagement = fg.parts["2"]?.final?.engagement


        when (gradeTypeId) {
            1 -> {
                itemView.tvPartOneFinalGrade.text = partOneFinalGrade.toString()
                itemView.tvPartOneAverageGrade.text =
                    if (partOneAverageGrade != 0f)
                        String.format("(%.2f)", partOneAverageGrade)
                    else "/"

                itemView.tvPartTwoFinalGrade.text = partTwoFinalGrade.toString()
                itemView.tvPartTwoAverageGrade.text =
                    if (partTwoAverageGrade != 0f)
                        String.format("(%.2f)", partTwoAverageGrade)
                    else "/"
            }
            2 -> {
                itemView.tvPartOneFinalGrade.text = partOneFinalName ?: "/"
                itemView.tvPartOneAverageGrade.text = ""
                itemView.tvPartTwoFinalGrade.text = partTwoFinalName ?: "/"
                itemView.tvPartTwoAverageGrade.text = ""
            }
            5 -> {
                itemView.tvPartOneFinalGrade.text =
                    if (partOneFinalName != null) napredovanje + partOneFinalName else "/"
                itemView.tvPartOneAverageGrade.text =
                    if (partOneFinalEngagement != null) angazovanje + partOneFinalEngagement else "/"
                itemView.tvPartTwoFinalGrade.text =
                    if (partTwoFinalName != null) napredovanje + partTwoFinalName else "/"
                itemView.tvPartTwoAverageGrade.text =
                    if (partTwoFinalEngagement != null) angazovanje + partTwoFinalEngagement else "/"
            }
            else -> {
                itemView.tvPartOneFinalGrade.text = "/"
                itemView.tvPartOneAverageGrade.text = "/"
                itemView.tvPartTwoFinalGrade.text = "/"
                itemView.tvPartTwoAverageGrade.text = "/"
            }
        }

        bindGrades(itemView.tvPartOneGrades, fg.parts["1"]?.grades)
        bindGrades(itemView.tvPartTwoGrades, fg.parts["2"]?.grades)
    }

    private fun bindGrades(tv: TextView, grades: List<FullGrade>?) {
        grades.let {
            val sb = StringBuffer()
            it?.forEachIndexed { _, i ->
                if (i.descriptive) {
                    if (i.evaluationElement?.name != null) {
                        sb.append(i.evaluationElement.name).append("\n")
                    } else {
                        sb.append(i.fullGrade).append("  ")
                    }

                } else {
                    sb.append(i.grade).append(" ")
                }
            }
            tv.text = sb.toString()
        }
    }

    companion object {
        fun init(parent: ViewGroup): GradesViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_subject_activity2, parent, false)
            return GradesViewHolder(view)
        }
    }
}