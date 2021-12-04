package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.databinding.RowSubjectActivity2Binding
import rs.tafilovic.mojesdnevnik.model.FullGrade
import rs.tafilovic.mojesdnevnik.model.FullGrades

class GradesViewHolder(private val itemBinding: RowSubjectActivity2Binding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    val napredovanje = "Напредовање\n"
    val angazovanje = "\nАнгажовање\n"

    fun bind(fg: FullGrades) {
        itemBinding.tvClassName.text = fg.course

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
                itemBinding.tvPartOneFinalGrade.text = partOneFinalGrade.toString()
                itemBinding.tvPartOneAverageGrade.text =
                    if (partOneAverageGrade != 0f)
                        String.format("(%.2f)", partOneAverageGrade)
                    else "/"

                itemBinding.tvPartTwoFinalGrade.text = partTwoFinalGrade.toString()
                itemBinding.tvPartTwoAverageGrade.text =
                    if (partTwoAverageGrade != 0f)
                        String.format("(%.2f)", partTwoAverageGrade)
                    else "/"
            }
            2 -> {
                itemBinding.tvPartOneFinalGrade.text = partOneFinalName ?: "/"
                itemBinding.tvPartOneAverageGrade.text = ""
                itemBinding.tvPartTwoFinalGrade.text = partTwoFinalName ?: "/"
                itemBinding.tvPartTwoAverageGrade.text = ""
            }
            5 -> {
                itemBinding.tvPartOneFinalGrade.text =
                    if (partOneFinalName != null) napredovanje + partOneFinalName else "/"
                itemBinding.tvPartOneAverageGrade.text =
                    if (partOneFinalEngagement != null) angazovanje + partOneFinalEngagement else "/"
                itemBinding.tvPartTwoFinalGrade.text =
                    if (partTwoFinalName != null) napredovanje + partTwoFinalName else "/"
                itemBinding.tvPartTwoAverageGrade.text =
                    if (partTwoFinalEngagement != null) angazovanje + partTwoFinalEngagement else "/"
            }
            else -> {
                itemBinding.tvPartOneFinalGrade.text = "/"
                itemBinding.tvPartOneAverageGrade.text = "/"
                itemBinding.tvPartTwoFinalGrade.text = "/"
                itemBinding.tvPartTwoAverageGrade.text = "/"
            }
        }

        bindGrades(itemBinding.tvPartOneGrades, fg.parts["1"]?.grades)
        bindGrades(itemBinding.tvPartTwoGrades, fg.parts["2"]?.grades)
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
            val itemBinding: RowSubjectActivity2Binding = RowSubjectActivity2Binding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return GradesViewHolder(itemBinding)
        }
    }
}