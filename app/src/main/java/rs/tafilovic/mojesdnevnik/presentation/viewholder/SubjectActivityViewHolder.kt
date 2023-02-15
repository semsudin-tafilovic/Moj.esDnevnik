package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.RowSubjectActivityBinding
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.presentation.res.icon_bad
import rs.tafilovic.mojesdnevnik.presentation.res.icon_good
import rs.tafilovic.mojesdnevnik.presentation.res.icon_neutral
import rs.tafilovic.mojesdnevnik.util.FontManager

class SubjectActivityViewHolder(private val itemBinding: RowSubjectActivityBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    companion object {
        fun init(parent: ViewGroup): SubjectActivityViewHolder {
            val itemBinding = RowSubjectActivityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SubjectActivityViewHolder(itemBinding)
        }
    }

    fun bind(subjectActivity: SubjectActivity) {

        itemBinding.tvClassName.text = subjectActivity.course
        itemBinding.tvPartOneGrades.text = null
        itemBinding.tvPartTwoGrades.text = null

        subjectActivity.parts["1"]?.activities
            ?.map { getIconText(itemView.context, it.type) }
            ?.forEach {
                itemBinding.tvPartOneGrades.append(it)
                itemBinding.tvPartOneGrades.append(" ")
            }

        subjectActivity.parts["2"]?.activities
            ?.map { getIconText(itemView.context, it.type) }
            ?.forEach {
                itemBinding.tvPartTwoGrades.append(it)
                itemBinding.tvPartTwoGrades.append(" ")
            }

        itemBinding.tvPartOneGrades.typeface = FontManager.getTypeFace(itemView.context)

        itemBinding.tvPartTwoGrades.typeface = FontManager.getTypeFace(itemView.context)

        itemBinding.cardFinalOne.visibility = View.GONE
        itemBinding.cardFinalTwo.visibility = View.GONE
    }

    private fun getIcon(type: Int): String {
        return when (type) {
            1 -> icon_good
            2 -> icon_bad
            else -> icon_neutral
        }
    }

    private fun getIconText(context: Context, type: Int): Spannable {
        val icon = getIcon(type)
        val color = getColor(context, type)
        return SpannableString(icon).apply {
            setSpan(ForegroundColorSpan(color), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun getColor(context: Context, type: Int): Int {
        return when (type) {
            1 -> ContextCompat.getColor(context, R.color.green)
            2 -> ContextCompat.getColor(context, R.color.red)
            else -> ContextCompat.getColor(context, R.color.orange)
        }
    }
}