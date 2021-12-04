package rs.tafilovic.mojesdnevnik.presentation.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.RowActivityDetailsBinding
import rs.tafilovic.mojesdnevnik.model.Activities
import rs.tafilovic.mojesdnevnik.presentation.res.icon_bad
import rs.tafilovic.mojesdnevnik.presentation.res.icon_good
import rs.tafilovic.mojesdnevnik.presentation.res.icon_neutral
import rs.tafilovic.mojesdnevnik.util.FontManager
import rs.tafilovic.mojesdnevnik.util.fromHtml

class DetailsViewHolder(private val binding: RowActivityDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(activity: Activities) {
        binding.tvIcon.append(getIconText(itemView.context, activity.type))
        binding.tvType.append(activity.name)
        binding.tvDate.append(activity.date)
        binding.tvDescription.append(activity.note.fromHtml())

        binding.tvIcon.typeface =
            FontManager.getTypeFace(itemView.context, FontManager.FONTAWESOME)

        binding.tvIcon.typeface =
            FontManager.getTypeFace(itemView.context, FontManager.FONTAWESOME)
    }

    private fun getIcon(type: Int): String {
        return when (type) {
            1 -> icon_good
            2 -> icon_bad
            else -> icon_neutral
        }
    }

    private fun getIconText(context: Context, type: Int): Spannable {
        val icon = getIcon(type)
        val color = getColor(context, type)
        return SpannableString(icon).apply {
            setSpan(ForegroundColorSpan(color), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun getColor(context: Context, type: Int): Int {
        return when (type) {
            1 -> ContextCompat.getColor(context, R.color.green)
            2 -> ContextCompat.getColor(context, R.color.red)
            else -> ContextCompat.getColor(context, R.color.orange)
        }
    }

    companion object {
        fun create(parent: ViewGroup): DetailsViewHolder {
            val binding = RowActivityDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return DetailsViewHolder(binding)
        }
    }
}