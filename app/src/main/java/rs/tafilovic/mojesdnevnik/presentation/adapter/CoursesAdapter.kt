package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.CoursesViewHolder
import rs.tafilovic.mojesdnevnik.model.MainCourse

class CoursesAdapter : ListAdapter<MainCourse, CoursesViewHolder>(MainCourse.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        return CoursesViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}