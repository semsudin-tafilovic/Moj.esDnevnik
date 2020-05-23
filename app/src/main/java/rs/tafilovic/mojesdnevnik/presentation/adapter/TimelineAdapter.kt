package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.EventViewHolder
import rs.tafilovic.mojesdnevnik.model.Event

class TimelineAdapter : PagedListAdapter<Event, EventViewHolder>(Event.DIF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.init(parent, viewType)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position),getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        val event = getItem(position)
        return when (event?.type) {
            EventViewHolder.EVENT_TYPE_GRADE -> if (event.grade.gradeTypeId == 1) EventViewHolder.ITEM_GRADE else EventViewHolder.ITEM_GRADE_DESC
            EventViewHolder.EVENT_TYPE_ACTIVITY -> EventViewHolder.ITEM_ACTIVITY
            EventViewHolder.EVENT_TYPE_FINAL_GRADE -> if (event.grade.gradeTypeId == 1) EventViewHolder.ITEM_FINAL_GRADE else EventViewHolder.ITEM_FINAL_GRADE_DESC
            else -> EventViewHolder.ITEM_ABSENT
        }
    }
}