package rs.tafilovic.mojesdnevnik.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build

/**
 *  Check network status and subscribe on network changes <p/>
 *
 *  Example of usage: <p/>
 *
 *      // create network util instance to observe connection changes <p/>
 *      networkUtil = NetworkUtil(this) { isAvailable ->
 *           //do action when network availability changed
 *      }
 *
 *      // subscribe to changes in onCreate, onResume, etc...
 *      networkUtil.registerListener()
 *
 *      // unregister listener on exit from activity or fragment (onDestroy, onPause...)
 *      networkUtil.unregisterListener()
 *
 */
class NetworkUtil(private val context: Context, onNetworkStatusChanged: (Boolean) -> Unit) {

    private lateinit var connectivityManager: ConnectivityManager

    /**
     * This callback is used for API 24+
     */
    private val networkCallback =
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onNetworkStatusChanged(true)
            }

            override fun onUnavailable() {
                onNetworkStatusChanged(false)
            }

            override fun onLost(network: Network) {
                onUnavailable()
            }
        }

    /**
     * Receiver is used on devices with API 23 and bellow
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val connected = context.isConnected()
            onNetworkStatusChanged(connected)
        }
    }

    /**
     * Register listener for network changes. For devices from API 16-23 [BroadcastReceiver] is registered,
     * and for devices with API 24+ we are subscribing with [ConnectivityManager.registerDefaultNetworkCallback] to get network changes
     */
    @SuppressLint("NewApi")
    fun registerListener() {

        // register broadcast receiver to listen on network changes for SDK API 16 - 20
        context.runWithApi(16, 23) {
            @Suppress("DEPRECATION")
            context.registerReceiver(
                receiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }

        // register network callback to listen on network changes for SDK API 24+
        context.runWithApi(24) {
            connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.activeNetwork == null) networkCallback.onUnavailable()
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    /**
     * Call this method in [FragmentActivity#onDestroy()] or in [Fragment#onViewDestroyed()] to unsubscribe from changes
     */
    @SuppressLint("NewApi")
    fun unregisterListener() {
        context.runWithApi(16, 23) {
            context.unregisterReceiver(receiver)
        }

        context.runWithApi(24) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}