@file:Suppress("MemberVisibilityCanBePrivate")

package rs.tafilovic.mojesdnevnik.util

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import rs.tafilovic.mojesdnevnik.R

class FontManager {

    companion object {
        const val FONTAWESOME = R.font.font_awesome

        fun getTypeFace(context: Context, @FontRes font: Int = FONTAWESOME): Typeface? {
            return ResourcesCompat.getFont(context, font)
        }
    }
}