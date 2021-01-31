package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_behavior.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.Behavior
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class BehaviorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val sdf = SimpleDateFormat("dd. MM. yyyy.", Locale.getDefault())

    fun bind(behavior: Behavior) {
        itemView.apply {
            tvDate.text = behavior.date;
            tvName.text = behavior.name
            tvNote.text = behavior.note
            tvNote.visibility = if (behavior.note.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun init(parent: ViewGroup): BehaviorsViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_behavior, parent, false)
            return BehaviorsViewHolder(view)
        }
    }
}