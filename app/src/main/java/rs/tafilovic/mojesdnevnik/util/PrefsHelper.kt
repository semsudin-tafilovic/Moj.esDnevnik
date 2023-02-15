package rs.tafilovic.mojesdnevnik.util

import android.content.Context
import rs.tafilovic.mojesdnevnik.model.Cookie
import java.io.File

class PrefsHelper private constructor(context: Context, key: String) {

    private val cacheDir = context.cacheDir
    private val responseDir = File(cacheDir, "response")
    private val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    fun getLong(key: String): Long {
        return prefs.getLong(key, 0)
    }

    fun getString(key: String): String? {
        return prefs.getString(key, "")
    }

    fun setLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun removeCredentials(deleteUsernamePassword: Boolean = false) {

        // remove user credentials
        val editor = prefs.edit()

        if (deleteUsernamePassword) {
            editor.remove(Cookie.PASSWORD)
            editor.remove(Cookie.USERNAME)
        }
        editor.remove(Cookie.COOKIE)
        editor.remove(Cookie.COOKIE_EXPIRE)
        editor.apply()

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