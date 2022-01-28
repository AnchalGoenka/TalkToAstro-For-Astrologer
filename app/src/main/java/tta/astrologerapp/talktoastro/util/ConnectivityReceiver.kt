package tta.astrologerapp.talktoastro.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


/**

 * Created by Vivek Singh on 2019-09-07.

 */
class ConnectivityReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {

        if (tta.astrologerapp.talktoastro.util.ConnectivityReceiver.Companion.connectivityReceiverListener != null) {
            tta.astrologerapp.talktoastro.util.ConnectivityReceiver.Companion.connectivityReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(context))
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: tta.astrologerapp.talktoastro.util.ConnectivityReceiver.ConnectivityReceiverListener? = null
    }
}