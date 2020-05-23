package rs.tafilovic.mojesdnevnik.util

import android.util.Log

class Logger {
    companion object {
        fun d(tag: String, msg: String?) {
            Log.d(tag, msg?: "null")
        }

        fun e(tag: String, msg: String?) {
            Log.e(tag, msg?: "null")
        }

        fun i(tag: String, msg: String?) {
            Log.i(tag, msg?: "null")
        }

        fun w(tag: String, msg: String?) {
            Log.w(tag, msg?: "null")
        }

        fun v(tag: String, msg: String?) {
            Log.v(tag, msg?: "null")
        }
    }
}