package rs.tafilovic.mojesdnevnik.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import rs.tafilovic.mojesdnevnik.R

class ColorHelper {

    fun TextView.setColor(type: Int) {
        val color = when (type) {
            1 -> ContextCompat.getColor(this.context, R.color.green)
            2 -> ContextCompat.getColor(this.context, R.color.red)
            else -> ContextCompat.getColor(this.context, R.color.orange)
        }
        this.setTextColor(color)
    }
}