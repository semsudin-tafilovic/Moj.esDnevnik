package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.databinding.RowAbsentsBinding
import rs.tafilovic.mojesdnevnik.model.Absent

class AbsentsAdapter : ListAdapter<Absent, AbsentsViewHolder>(Absent.DIF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsentsViewHolder {
        return AbsentsViewHolder.init(parent)
    }

    override fun onBindViewHolder(holder: AbsentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AbsentsViewHolder(private val itemBinding: RowAbsentsBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(absent: Absent) {
        itemView.apply {
            itemBinding.tvClassName.text = absent.classCourseName
            itemBinding.tvStatusName.text = absent.absentStatus
            itemBinding.tvCount.text = absent.absentDetails.size.toString()
        }
    }

    companion object {
        fun init(parent: ViewGroup): AbsentsViewHolder {
            val itemBinding= RowAbsentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AbsentsViewHolder(itemBinding)
        }
    }
}
