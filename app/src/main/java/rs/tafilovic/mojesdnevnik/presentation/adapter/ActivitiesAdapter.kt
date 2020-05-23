package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.SubjectActivityViewHolder
import rs.tafilovic.mojesdnevnik.model.SubjectActivity

class ActivitiesAdapter :
    ListAdapter<SubjectActivity, SubjectActivityViewHolder>(SubjectActivity.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectActivityViewHolder {
        return SubjectActivityViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: SubjectActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
