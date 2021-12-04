package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.model.SubjectActivity
import rs.tafilovic.mojesdnevnik.presentation.viewholder.SubjectActivityViewHolder

class ActivitiesAdapter(private val callback: (SubjectActivity) -> Unit) :
    ListAdapter<SubjectActivity, SubjectActivityViewHolder>(SubjectActivity.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectActivityViewHolder {
        return SubjectActivityViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: SubjectActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            callback(getItem(position))
        }
    }
}
