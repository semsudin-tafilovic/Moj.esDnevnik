package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_event.view.*
import kotlinx.android.synthetic.main.row_event.view.tvClassName
import kotlinx.android.synthetic.main.row_event.view.tvDate
import kotlinx.android.synthetic.main.row_event.view.tvGrade
import kotlinx.android.synthetic.main.row_event.view.tvNote
import kotlinx.android.synthetic.main.row_event_absent.view.*
import kotlinx.android.synthetic.main.row_event_absent.view.tvStatus
import kotlinx.android.synthetic.main.row_event_absent.view.viewColorIndicatorAbsent
import kotlinx.android.synthetic.main.row_event_final_grade_desc.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.presentation.res.icon_bad
import rs.tafilovic.mojesdnevnik.presentation.res.icon_good
import rs.tafilovic.mojesdnevnik.presentation.res.icon_neutral
import rs.tafilovic.mojesdnevnik.util.FontManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.row_event.view.viewColorIndicator as viewColorIndicator1

class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val sdf = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)

        val EVENT_TYPE_GRADE = "grade"
        val EVENT_TYPE_ACTIVITY = "activity"
        val EVENT_TYPE_ABSENT = "absent"
        val EVENT_TYPE_FINAL_GRADE = "final-grade"
        val EVENT_TYPE_DESC_GRADE = "grade_desc"
        val EVENT_TYPE_FINAL_DESC_GRADE = "final_desc_grade"

        val ITEM_GRADE = 0
        val ITEM_ACTIVITY = 1
        val ITEM_FINAL_GRADE = 2
        val ITEM_ABSENT = 3
        val ITEM_GRADE_DESC = 4
        val ITEM_FINAL_GRADE_DESC = 5

        val appearance_text = androidx.appcompat.R.style.TextAppearance_AppCompat_Display3
        val appearance_icon = androidx.appcompat.R.style.TextAppearance_AppCompat_Display2

        fun init(parent: ViewGroup, itemType: Int): EventViewHolder {
            val resourceView = when (itemType) {
                ITEM_ABSENT -> R.layout.row_event_absent
                ITEM_GRADE_DESC -> R.layout.row_event_grade_desc
                ITEM_FINAL_GRADE_DESC -> R.layout.row_event_final_grade_desc
                else -> R.layout.row_event
            }
            val view = LayoutInflater.from(parent.context).inflate(resourceView, parent, false)
            return EventViewHolder(view)
        }
    }

    fun bind(event: Event?, type: Int) {
        if (event == null) return

        itemView.apply {
            tvDate.text = event.date
            tvClassName.text = event.course
            when (type) {
                ITEM_FINAL_GRADE,
                ITEM_GRADE -> {
                    tvGrade.setAppearance(appearance_text)
                    tvGrade.text = event.grade.value.toString()
                    tvGradeName.text = event.fullGrade
                    tvNote.text = event.note
                    viewColorIndicator.setBackgroundColor(Color.BLUE)
                }
                ITEM_ACTIVITY -> {
                    tvGrade.setAppearance(appearance_icon)
                    tvGrade.text = getIcon(event.cssClass)
                    tvGradeName.visibility = View.GONE
                    tvGrade.typeface =
                        FontManager.getTypeFace(tvGrade.context, FontManager.FONTAWESOME)
                    tvNote.text = event.note
                    viewColorIndicator.setBackgroundColor(Color.GRAY)
                }
                ITEM_ABSENT -> {
                    tvClassNumber.text = "${event.schoolHour}. Čas"
                    tvStatus.text = "Izostanak (${event.status})"
                    tvStatus.setTextColor(Color.parseColor("#3AA22E"))
                    tvNote.text = event.workHourNote
                    viewColorIndicatorAbsent.setBackgroundColor(ContextCompat.getColor(viewColorIndicatorAbsent.context, R.color.orange))
                    //viewColorIndicatorAbsent.setBackgroundColor(Color.parseColor("#3AA22E"))
                }
                ITEM_GRADE_DESC -> {
                    tvGrade.text =
                        if (event.evaluationElementCourse != null)
                            event.evaluationElementCourse.getEvaluationElement()
                        else event.fullGrade
                }
                ITEM_FINAL_GRADE_DESC -> {
                    tvGrade.text = event.grade.name
                    tvStatus.text = "Zaključna ocena (${event.schoolyearPart})"
                    viewColorIndicator.setBackgroundColor(ContextCompat.getColor(viewColorIndicator.context,R.color.colorPrimary))
                }
            }
        }
    }

    private fun TextView.setAppearance(appearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tvGrade.setTextAppearance(appearance)
        else
            tvGrade.setTextAppearance(tvGrade.context, appearance)
    }

    private fun getIcon(cssClass: String): String {
        return when (cssClass) {
            "bad" -> icon_bad
            "meh" -> icon_neutral
            else -> icon_good
        }
    }

}