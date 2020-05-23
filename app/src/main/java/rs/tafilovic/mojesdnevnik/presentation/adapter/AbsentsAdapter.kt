package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.AbsentsViewHolder
import rs.tafilovic.mojesdnevnik.model.Absent

class AbsentsAdapter : ListAdapter<Absent, AbsentsViewHolder>(Absent.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsentsViewHolder {
        return AbsentsViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: AbsentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
