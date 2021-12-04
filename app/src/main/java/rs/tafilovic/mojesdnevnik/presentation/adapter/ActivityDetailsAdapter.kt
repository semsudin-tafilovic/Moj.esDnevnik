package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.model.Activities
import rs.tafilovic.mojesdnevnik.presentation.viewholder.DetailsViewHolder

class ActivityDetailsAdapter : RecyclerView.Adapter<DetailsViewHolder>() {

    val activities = mutableListOf<Activities>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun submitItems(activities: List<Activities>) {
        this.activities.clear()
        if (!activities.isNullOrEmpty()) {
            this.activities.addAll(activities)
            notifyItemRangeInserted(0, activities.size)
        }
    }

}