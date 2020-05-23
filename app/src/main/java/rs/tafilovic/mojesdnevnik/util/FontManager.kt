package rs.tafilovic.mojesdnevnik.util

import android.content.Context
import android.graphics.Typeface

class FontManager {

    companion object {
        val ROOT = "fonts/"
        val FONTAWESOME = ROOT + "fa-regular-400.ttf"

        fun getTypeFace(context: Context, font: String): Typeface {
            return Typeface.createFromAsset(context.assets, font)
        }
    }
}