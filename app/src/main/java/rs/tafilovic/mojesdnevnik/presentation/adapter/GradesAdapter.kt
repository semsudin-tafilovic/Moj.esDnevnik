package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.GradesViewHolder
import rs.tafilovic.mojesdnevnik.model.FullGrades
import rs.tafilovic.mojesdnevnik.model.FullGrades.Companion.DIF_UTIL

class GradesAdapter : ListAdapter<FullGrades, GradesViewHolder>(DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        return GradesViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}