package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import rs.tafilovic.mojesdnevnik.rest.ApiClient
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.util.NetworkUtil

abstract class BaseActivity : AppCompatActivity() {

    protected open val TAG = this::class.java.name

    private var networkUtil: NetworkUtil? = null

    fun showToast(msg: String?) {
        if (msg != null)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        networkUtil = NetworkUtil(this) {
            runOnUiThread {
                onConnectionChanged(it)
            }
        }
        networkUtil?.registerListener()
    }

    override fun onPause() {
        super.onPause()
        networkUtil?.unregisterListener()
    }

    protected open fun onConnectionChanged(connected: Boolean) {
        Logger.d(TAG, "onConnectionChanged: $connected")
        ApiClient.isConnected.set(connected)
    }
}