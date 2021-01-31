@file:Suppress("DEPRECATION")

package rs.tafilovic.mojesdnevnik.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import androidx.core.text.toSpanned
import androidx.preference.PreferenceManager

/**
 * File with some helper methods (mainly extension methods)
 */


/**
 * Applies html formatting to String and returns trimmed Spanned
 */
fun String?.fromHtml(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).trim().toSpanned()
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this).trim().toSpanned()
    }
}

/**
 * Substring spanned to specific length with adding 3 dots at the end if string is larger then provided length
 */
fun Spanned?.trimWithDots(length: Int): Spanned? {
    if (this.isNullOrEmpty()) return "".toSpanned()
    return (if (this.length < length) SpannableString(this)
    else SpannableString("${this.substring(0, length)}...")).trim().toSpanned()
}

/**
 * Check if device has connection. Only for API SDK 28 and bellow
 * [NetworkInfo], [NetworkInfo.isConnected] and [ConnectivityManager.getNetworkInfo] are deprecated in API 29
 */
@Suppress("DEPRECATION")
fun Context.isConnected(): Boolean {
    val cm: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnected == true
}

/**
 * Execute some [action] if Device SDK Version is in range between [minSdk] and [maxSdk]
 * [maxSdk] is optional. If not provided, then [maxSdk] is latest [TargetSdkVersion] from build.gradle
 */
@SuppressLint("NewApi")
fun Context.runWithApi(minSdk: Int, maxSdk: Int? = null, action: () -> Unit) {
    val targetSdk =
        maxSdk ?: this.packageManager.getApplicationInfo(this.packageName, 0).targetSdkVersion
    if (Build.VERSION.SDK_INT in minSdk..targetSdk) {
        action()
    }
}

fun Context.getPrefsBoolean(name: String): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(name, false)
}

fun Context.setPrefsBoolean(name: String, value: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(name, value).apply()
}
