@file:Suppress("DEPRECATION")

package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.RowEventAbsentBinding
import rs.tafilovic.mojesdnevnik.databinding.RowEventBinding
import rs.tafilovic.mojesdnevnik.databinding.RowEventFinalGradeDescBinding
import rs.tafilovic.mojesdnevnik.databinding.RowEventGradeDescBinding
import rs.tafilovic.mojesdnevnik.model.Event
import rs.tafilovic.mojesdnevnik.presentation.adapter.TimelineAdapter.Companion.appearance_icon
import rs.tafilovic.mojesdnevnik.presentation.adapter.TimelineAdapter.Companion.getIcon
import rs.tafilovic.mojesdnevnik.presentation.adapter.TimelineAdapter.Companion.setAppearance
import rs.tafilovic.mojesdnevnik.presentation.res.icon_bad
import rs.tafilovic.mojesdnevnik.presentation.res.icon_good
import rs.tafilovic.mojesdnevnik.presentation.res.icon_neutral
import rs.tafilovic.mojesdnevnik.util.FontManager
import java.text.DateFormat
import java.text.SimpleDateFormat

@Suppress("PrivatePropertyName")
class TimelineAdapter : PagedListAdapter<Event, RecyclerView.ViewHolder>(Event.DIF_UTIL) {

    val sdf = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)

    private val EVENT_TYPE_GRADE = "grade"
    private val EVENT_TYPE_ACTIVITY = "activity"
    private val EVENT_TYPE_ABSENT = "absent"
    private val EVENT_TYPE_FINAL_GRADE = "final-grade"
    private val EVENT_TYPE_DESC_GRADE = "grade_desc"
    private val EVENT_TYPE_FINAL_DESC_GRADE = "final_desc_grade"

    private val ITEM_GRADE = 0
    private val ITEM_ACTIVITY = 1
    private val ITEM_FINAL_GRADE = 2
    private val ITEM_ABSENT = 3
    private val ITEM_GRADE_DESC = 4
    private val ITEM_FINAL_GRADE_DESC = 5

    companion object {
        val appearance_text = androidx.appcompat.R.style.TextAppearance_AppCompat_Display3
        val appearance_icon = androidx.appcompat.R.style.TextAppearance_AppCompat_Display2

        fun TextView.setAppearance(appearance: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                this.setTextAppearance(appearance)
            else
                this.setTextAppearance(this.context, appearance)
        }

        fun getIcon(cssClass: String): String {
            return when (cssClass) {
                "bad" -> icon_bad
                "meh" -> icon_neutral
                else -> icon_good
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_ACTIVITY -> {
                return ActivityViewHolder.init(parent)
            }
            ITEM_ABSENT -> {
                return AbsentViewHolder.init(parent)
            }
            ITEM_GRADE_DESC -> {
                return GradeDescViewHolder.init(parent)
            }
            ITEM_FINAL_GRADE_DESC -> {
                return FinalGradeDescViewHolder.init(parent)
            }
            else -> { //ITEM_GRADE, ITEM_FINAL_GRADE
                return GradeViewHolder.init(parent)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position) ?: return

        when (holder) {
            is ActivityViewHolder -> holder.bind(event)
            is AbsentViewHolder -> holder.bind(event)
            is GradeDescViewHolder -> holder.bind(event)
            is FinalGradeDescViewHolder -> holder.bind(event)
            is GradeViewHolder -> holder.bind(event)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val event = getItem(position)
        return when (event?.type) {
            EVENT_TYPE_GRADE -> if (event.grade.gradeTypeId == 1) ITEM_GRADE else ITEM_GRADE_DESC
            EVENT_TYPE_ACTIVITY -> ITEM_ACTIVITY
            EVENT_TYPE_FINAL_GRADE -> if (event.grade.gradeTypeId == 1) ITEM_FINAL_GRADE else ITEM_FINAL_GRADE_DESC
            else -> ITEM_ABSENT
        }
    }
}

class AbsentViewHolder(private val itemBinding: RowEventAbsentBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(event: Event) {
        itemBinding.tvDate.text = event.date
        itemBinding.tvClassName.text = event.course
        itemBinding.tvClassNumber.text = "${event.schoolHour}. Čas"
        itemBinding.tvStatus.text = "Izostanak (${event.status})"
        itemBinding.tvStatus.setTextColor(Color.parseColor("#3AA22E"))
        itemBinding.tvNote.text = event.workHourNote
        itemBinding.viewColorIndicatorAbsent.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                R.color.orange
            )
        )
    }

    companion object {
        fun init(parent: ViewGroup): AbsentViewHolder {
            val itemBinding =
                RowEventAbsentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AbsentViewHolder(itemBinding)
        }
    }
}

class ActivityViewHolder(private val itemBinding: RowEventBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(event: Event) {
        itemBinding.tvDate.text = event.date
        itemBinding.tvClassName.text = event.course
        itemBinding.tvGrade.setAppearance(appearance_icon)
        itemBinding.tvGrade.text = getIcon(event.cssClass)
        itemBinding.tvGradeName.visibility = View.GONE
        itemBinding.tvGrade.typeface =
            FontManager.getTypeFace(itemView.context, FontManager.FONTAWESOME)
        itemBinding.tvNote.text = event.note
        itemBinding.viewColorIndicator.setBackgroundColor(Color.GRAY)
    }

    companion object {
        fun init(parent: ViewGroup): ActivityViewHolder {
            val itemBinding =
                RowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ActivityViewHolder(itemBinding)
        }
    }
}

class GradeViewHolder(private val itemBinding: RowEventBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(event: Event) {
        itemBinding.tvDate.text = event.date
        itemBinding.tvClassName.text = event.course
        itemBinding.tvGrade.setAppearance(TimelineAdapter.appearance_text)
        itemBinding.tvGrade.text = event.grade.value.toString()
        itemBinding.tvGradeName.text = event.fullGrade
        itemBinding.tvNote.text = event.note
        itemBinding.viewColorIndicator.setBackgroundColor(Color.BLUE)
    }

    companion object {
        fun init(parent: ViewGroup): GradeViewHolder {
            val itemBinding =
                RowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GradeViewHolder(itemBinding)
        }
    }
}

class GradeDescViewHolder(private val itemBinding: RowEventGradeDescBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(event: Event) {
        itemBinding.tvDate.text = event.date
        itemBinding.tvClassName.text = event.course
        itemBinding.tvGrade.text =
            if (event.evaluationElementCourse != null)
                event.evaluationElementCourse.getEvaluationElement()
            else event.fullGrade
    }

    companion object {
        fun init(parent: ViewGroup): GradeDescViewHolder {
            val itemBinding =
                RowEventGradeDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GradeDescViewHolder(itemBinding)
        }
    }
}

class FinalGradeDescViewHolder(private val itemBinding: RowEventFinalGradeDescBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(event: Event) {
        itemBinding.tvDate.text = event.date
        itemBinding.tvClassName.text = event.course
        itemBinding.tvGrade.text = event.grade.name
        itemBinding.tvStatus.text = "Zaključna ocena (${event.schoolyearPart})"
        itemBinding.viewColorIndicator.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                R.color.colorPrimary
            )
        )
    }

    companion object {
        fun init(parent: ViewGroup): FinalGradeDescViewHolder {
            val itemBinding = RowEventFinalGradeDescBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FinalGradeDescViewHolder(itemBinding)
        }
    }
}