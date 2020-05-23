package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_absents.view.*
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.Absent

class AbsentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(absent: Absent) {
        itemView.apply {
            tvClassName.text = absent.classCourseName
            tvStatusName.text = absent.absentStatus
            tvCount.text = absent.absentDetails.size.toString()
        }
    }

    companion object {
        fun init(parent: ViewGroup): AbsentsViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_absents, parent, false)
            return AbsentsViewHolder(view)
        }
    }
}