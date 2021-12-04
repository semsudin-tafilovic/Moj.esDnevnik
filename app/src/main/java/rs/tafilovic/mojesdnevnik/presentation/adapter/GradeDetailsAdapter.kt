package rs.tafilovic.mojesdnevnik.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.databinding.RowGradeDetailsBinding
import rs.tafilovic.mojesdnevnik.model.FullGrade
import rs.tafilovic.mojesdnevnik.util.fromHtml

class GradeDetailsAdapter :
    RecyclerView.Adapter<GradeDetailsViewHolder>() {

    val grades = mutableListOf<FullGrade>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeDetailsViewHolder {
        return GradeDetailsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GradeDetailsViewHolder, position: Int) {
        holder.bind(grades[position])
    }

    override fun getItemCount(): Int {
        return grades.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitItems(grades: List<FullGrade>?) {
        this.grades.clear()
        if (!grades.isNullOrEmpty()) {
            this.grades.addAll(grades)
        }
        notifyDataSetChanged()
    }
}

class GradeDetailsViewHolder(val binding: RowGradeDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(grade: FullGrade) {
        binding.tvFullGrade.text = grade.fullGrade
        binding.tvDescription.text = grade.note.fromHtml()
        binding.tvDate.text = grade.date
    }

    companion object {
        fun create(parent: ViewGroup): GradeDetailsViewHolder {
            val binding = RowGradeDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return GradeDetailsViewHolder(binding)
        }
    }
}