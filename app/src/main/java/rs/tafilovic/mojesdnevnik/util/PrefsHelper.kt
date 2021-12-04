package rs.tafilovic.mojesdnevnik.util

import android.content.Context
import rs.tafilovic.mojesdnevnik.model.Cookie
import java.io.File

class PrefsHelper private constructor(context: Context, key: String) {

    private val cacheDir = context.cacheDir
    private val responseDir = File(cacheDir, "response")
    private val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    fun getInt(key: String): Int {
        return prefs.getInt(key, 0)
    }

    fun getLong(key: String): Long {
        return prefs.getLong(key, 0)
    }

    fun getString(key: String): String? {
        return prefs.getString(key, "")
    }

    fun getBool(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun setLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun setBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun setStringSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    fun removeCredentials() {

        // remove user credentials
        prefs.edit()
            .remove(Cookie.PASSWORD)
            .remove(Cookie.USERNAME)
            .remove(Cookie.COOKIE)
            .remove(Cookie.COOKIE_EXPIRE)
            .apply()

        // delete cached responses
        if (responseDir.exists()) {
            val files = responseDir.listFiles()

            if (files != null)
                for (f in files)
                    f.delete()

            responseDir.delete()
        }
    }

    companion object {
        val DEFAULT_PREFS_KEY = "Moj.EsDnevnik.Prefs"

        @Volatile
        private var instance: PrefsHelper? = null

        fun getInstance(context: Context, key: String = DEFAULT_PREFS_KEY): PrefsHelper {
            if (instance == null)
                instance =
                    PrefsHelper(context, key)
            return instance!!
        }
    }
}