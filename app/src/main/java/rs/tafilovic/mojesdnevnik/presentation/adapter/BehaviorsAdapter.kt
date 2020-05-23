package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.tafilovic.mojesdnevnik.presentation.viewholder.BehaviorsViewHolder
import rs.tafilovic.mojesdnevnik.model.Behavior

class BehaviorsAdapter : ListAdapter<Behavior, BehaviorsViewHolder>(Behavior.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BehaviorsViewHolder {
        return BehaviorsViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: BehaviorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
