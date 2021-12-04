package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.databinding.RowBehaviorBinding
import rs.tafilovic.mojesdnevnik.model.Behavior
import java.text.SimpleDateFormat
import java.util.*

class BehaviorsAdapter : ListAdapter<Behavior, BehaviorsViewHolder>(Behavior.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BehaviorsViewHolder {
        return BehaviorsViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: BehaviorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BehaviorsViewHolder(private val itemBinding: RowBehaviorBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    val sdf = SimpleDateFormat("dd. MM. yyyy.", Locale.getDefault())

    fun bind(behavior: Behavior) {
        itemView.apply {
            itemBinding.tvDate.text = behavior.date
            itemBinding.tvName.text = behavior.name
            itemBinding.tvNote.text = behavior.note
            itemBinding.tvNote.visibility =
                if (behavior.note.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun init(parent: ViewGroup): BehaviorsViewHolder {
            val itemBinding =
                RowBehaviorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BehaviorsViewHolder(itemBinding)
        }
    }
}