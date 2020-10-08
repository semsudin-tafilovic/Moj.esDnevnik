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
import kotlinx.android.synthetic.main.row_subject_activity.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.presentation.res.icon_bad
import rs.tafilovic.mojesdnevnik.presentation.res.icon_good
import rs.tafilovic.mojesdnevnik.presentation.res.icon_neutral
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.util.FontManager

class SubjectActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun init(parent: ViewGroup): SubjectActivityViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_subject_activity, parent, false)
            return SubjectActivityViewHolder(view)
        }
    }

    fun bind(subjectActivity: SubjectActivity) {

        itemView.tvClassName.text = subjectActivity.course

        val firstPart = subjectActivity.parts["1"]?.activities
        val secondPart = subjectActivity.parts["2"]?.activities

        itemView.tvPartOneGrades.text=null
        firstPart?.forEach {
            itemView.tvPartOneGrades.text=null
            val spannable = getIconText(itemView.tvPartOneGrades.context, it.type)
            itemView.tvPartOneGrades.append(spannable)
            itemView.tvPartOneGrades.append(" ")
        }

        itemView.tvPartTwoGrades.text=null
        secondPart?.forEach {
            val spannable = getIconText(itemView.tvPartTwoGrades.context, it.type)
            itemView.tvPartTwoGrades.append(spannable)
            itemView.tvPartTwoGrades.append(" ")
        }

        itemView.tvPartOneGrades.typeface =
            FontManager.getTypeFace(itemView.context, FontManager.FONTAWESOME)

        itemView.tvPartTwoGrades.typeface =
            FontManager.getTypeFace(itemView.context, FontManager.FONTAWESOME)

        itemView.cardFinalOne.visibility = View.GONE
        itemView.cardFinalTwo.visibility = View.GONE
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